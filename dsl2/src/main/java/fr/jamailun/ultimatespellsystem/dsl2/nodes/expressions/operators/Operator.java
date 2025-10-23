package fr.jamailun.ultimatespellsystem.dsl2.nodes.expressions.operators;

import fr.jamailun.ultimatespellsystem.dsl2.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.type.Type;
import fr.jamailun.ultimatespellsystem.dsl2.tokenization.TokenPosition;
import org.jetbrains.annotations.NotNull;

/**
 * Operator : application of something on another expression.
 */
public abstract class Operator extends ExpressionNode {

    /**
     * Saved output type. Cache it here.
     */
    protected Type producedType;

    /**
     * New operator instance, at a position.
     * @param position position to use.
     */
    protected Operator(@NotNull TokenPosition position) {
        super(position);
    }

    /**
     * Check the type can be used for math.
     * @param a type to use.
     */
    protected void assertNotMathIncompatible(@NotNull Type a) {
        //TODO FIXME !!
        throw new UnsupportedOperationException("todo");
    }

    @Override
    public @NotNull Type getExpressionType() {
        return producedType;
    }
}
