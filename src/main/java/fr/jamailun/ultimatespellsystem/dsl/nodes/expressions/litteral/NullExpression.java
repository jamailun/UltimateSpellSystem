package fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.litteral;

import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Type;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenPosition;

public class NullExpression extends LiteralExpression {

    public NullExpression(TokenPosition pos) {
        super(pos);
    }

    @Override
    public Type getExpressionType() {
        return TypePrimitive.NULL.asType();
    }

    @Override
    public String toString() {
        return "NULL";
    }
}
