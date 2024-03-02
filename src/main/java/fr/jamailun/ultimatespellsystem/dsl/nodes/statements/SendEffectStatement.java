package fr.jamailun.ultimatespellsystem.dsl.nodes.statements;

import fr.jamailun.ultimatespellsystem.dsl.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.CollectionFilter;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypesContext;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.PreviousIndicator;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenStream;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenType;
import fr.jamailun.ultimatespellsystem.dsl.visitor.StatementVisitor;

import java.util.Optional;

public class SendEffectStatement extends SendStatement {

    protected final ExpressionNode effectType;
    protected final ExpressionNode effectDuration;
    protected final ExpressionNode effectPower; // nullable !

    public SendEffectStatement(ExpressionNode target, ExpressionNode effectType, ExpressionNode effectDuration, ExpressionNode effectPower) {
        super(target);
        this.effectType = effectType;
        this.effectDuration = effectDuration;
        this.effectPower = effectPower;
    }

    @Override
    public void validateTypes(TypesContext context) {
        super.validateTypes(context);

        assertExpressionType(effectType, CollectionFilter.MONO_ELEMENT, context, TypePrimitive.EFFECT_TYPE);
        assertExpressionType(effectDuration, CollectionFilter.MONO_ELEMENT, context, TypePrimitive.DURATION);
        if(effectPower != null)
            assertExpressionType(effectPower, CollectionFilter.MONO_ELEMENT, context, TypePrimitive.NUMBER);
    }

    public ExpressionNode getEffectType() {
        return effectType;
    }

    public ExpressionNode getEffectDuration() {
        return effectDuration;
    }

    public Optional<ExpressionNode> getEffectPower() {
        return Optional.ofNullable(effectPower);
    }

    @Override
    public void visit(StatementVisitor visitor) {
        visitor.handleSendEffect(this);
    }

    @PreviousIndicator(expected = {TokenType.EFFECT})
    public static SendEffectStatement parseSendEffect(ExpressionNode target, TokenStream tokens) {
        // Effect type
        ExpressionNode effectType = ExpressionNode.readNextExpression(tokens);

        // Expect FOR or NUMBER
        tokens.assertNextIs(TokenType.VALUE_NUMBER, TokenType.FOR);

        // power
        ExpressionNode effectPower = null;
        if(tokens.peek().getType() == TokenType.VALUE_NUMBER) {
            effectPower = ExpressionNode.readNextExpression(tokens);
        }

        // for + (duration)
        tokens.dropOrThrow(TokenType.FOR);
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
