package fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.litteral;

import fr.jamailun.ultimatespellsystem.dsl.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.variables.TypesContext;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenPosition;
import org.jetbrains.annotations.NotNull;

/**
 * A literal is a raw value in the code, such as a number, a string, ...
 * @param <T> the underlying type to handle.
 */
public abstract class LiteralExpression<T> extends ExpressionNode {

    protected final static String PREFIX = "<";
    protected final static String SUFFIX = ">";

    protected LiteralExpression(TokenPosition position) {
        super(position);
    }

    @Override
    public void validateTypes(@NotNull TypesContext context) {
        // Nothing to do
    }

    /**
     * Get the underlying value to use.
     * @return the literal.
     */
    public abstract T getRaw();

}
