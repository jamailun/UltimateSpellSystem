package fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.litteral;

import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Type;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.Token;
import fr.jamailun.ultimatespellsystem.dsl.visitor.ExpressionVisitor;
import org.jetbrains.annotations.NotNull;

/**
 * A raw boolean literal.
 */
public class BooleanExpression extends LiteralExpression<Boolean> {

    private final boolean rawValue;

    public BooleanExpression(@NotNull Token token) {
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
