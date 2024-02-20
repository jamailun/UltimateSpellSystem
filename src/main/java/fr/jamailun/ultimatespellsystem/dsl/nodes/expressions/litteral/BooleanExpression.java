package fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.litteral;

import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Type;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.Token;

public class BooleanExpression extends LiteralExpression {

    private final boolean rawValue;

    public BooleanExpression(Token token) {
        super(token.pos());
        this.rawValue = token.getContentBoolean();
    }

    public boolean getRawValue() {
        return rawValue;
    }

    @Override
    public Type getExpressionType() {
        return TypePrimitive.BOOLEAN.asType();
    }

    @Override
    public String toString() {
        return "{{"+(rawValue?"TRUE":"FALSE")+"}}";
    }
}
