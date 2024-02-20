package fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.litteral;

import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Type;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.Token;

public class NumberExpression extends LiteralExpression {

    private final double rawValue;

    public NumberExpression(Token token) {
        super(token.pos());
        this.rawValue = token.getContentNumber();
    }

    public double getRawValue() {
        return rawValue;
    }

    @Override
    public Type getExpressionType() {
        return TypePrimitive.NUMBER.asType();
    }

    @Override
    public String toString() {
        return "{{"+rawValue+"}}";
    }
}
