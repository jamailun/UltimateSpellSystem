package fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.litteral;

import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Type;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.Token;

public class RuntimeLiteral extends LiteralExpression {

    private final String value;

    public RuntimeLiteral(Token token) {
        super(token.pos());
        this.value = token.getContentString();
    }

    public String getValue() {
        return value;
    }

    @Override
    public Type getExpressionType() {
        return TypePrimitive.CUSTOM.asType();
    }

    @Override
    public String toString() {
        return "?'" + value + "'";
    }
}
