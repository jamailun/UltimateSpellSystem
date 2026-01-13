package fr.jamailun.ultimatespellsystem.dsl2.nodes.expressions.litteral;

import fr.jamailun.ultimatespellsystem.dsl2.nodes.type.Type;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.dsl2.tokenization.Token;
import fr.jamailun.ultimatespellsystem.dsl2.visitor.ExpressionVisitor;
import org.jetbrains.annotations.NotNull;

/**
 * A raw boolean literal.
 */
public class BooleanLiteral extends LiteralExpression<Boolean> {

    private final boolean rawValue;

    /**
     * New instance, from a token.
     * @param token non-null token to use.
     */
    public BooleanLiteral(@NotNull Token token) {
        super(token.pos());
        this.rawValue = token.getContentBoolean();
    }

    @Override
    public @NotNull Boolean getRaw() {
        return rawValue;
    }

    @Override
    public @NotNull Type getExpressionType() {
        return TypePrimitive.BOOLEAN.asType();
    }

    @Override
    public void visit(@NotNull ExpressionVisitor visitor) {
        visitor.handleBooleanLiteral(this);
    }

    @Override
    public String toString() {
        return PREFIX + (rawValue?"TRUE":"FALSE") + SUFFIX;
    }
}
