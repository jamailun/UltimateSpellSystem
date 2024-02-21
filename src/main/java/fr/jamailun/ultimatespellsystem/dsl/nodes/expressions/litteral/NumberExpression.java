package fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.litteral;

import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Type;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.Token;

public class NumberExpression extends LiteralExpression<Double> {

    private final Double rawValue;

    public NumberExpression(Token token) {
        super(token.pos());
        this.rawValue = token.getContentNumber();
    }

    @Override
    public Double getRaw() {
        return rawValue;
    }

    @Override
    public Type getExpressionType() {
        return TypePrimitive.NUMBER.asType();
    }

    @Override
    public String toString() {
        return PREFIX + rawValue + SUFFIX;
    }
}
