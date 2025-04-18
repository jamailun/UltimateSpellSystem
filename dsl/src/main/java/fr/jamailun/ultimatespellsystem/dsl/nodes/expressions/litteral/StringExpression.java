package fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.litteral;

import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Type;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.Token;
import fr.jamailun.ultimatespellsystem.dsl.visitor.ExpressionVisitor;
import org.jetbrains.annotations.NotNull;

/**
 * A raw string literal.
 */
public class StringExpression extends LiteralExpression<String> {

    private final String rawValue;

    /**
     * New raw string.
     * @param token token to use.
     */
    public StringExpression(@NotNull Token token) {
        super(token.pos());
        this.rawValue = token.getContentString();
    }

    @Override
    public String getRaw() {
        return rawValue;
    }

    @Override
    public @NotNull Type getExpressionType() {
        return TypePrimitive.STRING.asType();
    }

    @Override
    public String toString() {
        return PREFIX + "\"" + rawValue + "\"" + SUFFIX;
    }

    @Override
    public void visit(@NotNull ExpressionVisitor visitor) {
        visitor.handleStringLiteral(this);
    }
}
