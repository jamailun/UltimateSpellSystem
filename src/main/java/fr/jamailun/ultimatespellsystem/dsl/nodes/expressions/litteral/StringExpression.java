package fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.litteral;

import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Type;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.Token;

public class StringExpression extends LiteralExpression<String> {

    private final String rawValue;

    public StringExpression(Token token) {
        super(token.pos());
        this.rawValue = token.getContentString();
    }

    @Override
    public String getRaw() {
        return rawValue;
    }

    @Override
    public Type getExpressionType() {
        return TypePrimitive.STRING.asType();
    }

    @Override
    public String toString() {
        return PREFIX + "\"" + rawValue + "\"" + SUFFIX;
    }
}
