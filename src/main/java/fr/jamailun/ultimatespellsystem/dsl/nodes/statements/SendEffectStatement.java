package fr.jamailun.ultimatespellsystem.dsl.nodes.statements;

import fr.jamailun.ultimatespellsystem.dsl.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypesContext;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.PreviousIndicator;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenStream;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenType;
import fr.jamailun.ultimatespellsystem.dsl.visitor.Visitor;

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

        assertExpressionType(effectType, TypePrimitive.EFFECT_TYPE, context);
        assertExpressionType(effectDuration, TypePrimitive.DURATION, context);
        if(effectPower != null)
            assertExpressionType(effectPower, TypePrimitive.NUMBER, context);
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
    public void visit(Visitor visitor) {
        visitor.handleSendEffect(this);
    }

    @PreviousIndicator(expected = {TokenType.EFFECT})
    public static SendEffectStatement parseSendEffect(ExpressionNode target, TokenStream tokens) {
        // Effect type
        ExpressionNode effectType = ExpressionNode.readNextExpression(tokens);

        // power
        ExpressionNode effectPower = null;
        if(tokens.peek().getType() == TokenType.NUMBER) {
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
}
