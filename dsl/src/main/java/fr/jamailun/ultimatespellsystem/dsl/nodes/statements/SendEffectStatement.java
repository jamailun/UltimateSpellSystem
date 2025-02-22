package fr.jamailun.ultimatespellsystem.dsl.nodes.statements;

import fr.jamailun.ultimatespellsystem.dsl.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.litteral.EffectTypeExpression;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.litteral.StringExpression;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.CollectionFilter;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.PotionEffect;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypesContext;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.PreviousIndicator;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenStream;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenType;
import fr.jamailun.ultimatespellsystem.dsl.visitor.StatementVisitor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class SendEffectStatement extends SendStatement {

    @Getter private final ExpressionNode effectType;
    @Getter private final ExpressionNode effectDuration;
    private final ExpressionNode effectPower; // nullable !

    public SendEffectStatement(ExpressionNode target, ExpressionNode effectType, ExpressionNode effectDuration, ExpressionNode effectPower) {
        super(target);
        this.effectType = effectType;
        this.effectDuration = effectDuration;
        this.effectPower = effectPower;
    }

    @Override
    public void validateTypes(@NotNull TypesContext context) {
        super.validateTypes(context);

        assertExpressionType(effectType, CollectionFilter.MONO_ELEMENT, context, TypePrimitive.EFFECT_TYPE, TypePrimitive.STRING);
        assertExpressionType(effectDuration, CollectionFilter.MONO_ELEMENT, context, TypePrimitive.DURATION);
        if(effectPower != null)
            assertExpressionType(effectPower, CollectionFilter.MONO_ELEMENT, context, TypePrimitive.NUMBER);
    }

    public Optional<ExpressionNode> getEffectPower() {
        return Optional.ofNullable(effectPower);
    }

    @Override
    public void visit(@NotNull StatementVisitor visitor) {
        visitor.handleSendEffect(this);
    }

    @PreviousIndicator(expected = {TokenType.SEND/* + EFFECT */})
    public static @NotNull SendEffectStatement parseSendEffect(@NotNull ExpressionNode target, @NotNull TokenStream tokens) {
        // Effect type
        ExpressionNode effectType = ExpressionNode.readNextExpression(tokens);
        // But if string, remap to PotionEffect
        //FIXME this is dirty ! I should move the effect-type interpretation into the RUN part !
        if(effectType instanceof StringExpression strExpr) {
            effectType = new EffectTypeExpression(strExpr.firstTokenPosition(), PotionEffect.find(strExpr.getRaw()));
        }

        // If FOR just after : no power.
        ExpressionNode effectPower = null;
        if( ! tokens.dropOptional(TokenType.FOR)) {
            effectPower = ExpressionNode.readNextExpression(tokens);
            tokens.dropOrThrow(TokenType.FOR);
        }

        // Duration
        ExpressionNode effectDuration = ExpressionNode.readNextExpression(tokens);

        // Optional EOL
        tokens.dropOptional(TokenType.SEMI_COLON);

        // return
        return new SendEffectStatement(target, effectType, effectDuration, effectPower);
    }

    @Override
    public String toString() {
        return "SEND_EFFECT{to="+target+", effect=" + effectType + (effectPower==null?"":", power="+effectPower) + ", for=" + effectDuration + "}";
    }
}
