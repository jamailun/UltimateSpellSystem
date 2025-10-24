package fr.jamailun.ultimatespellsystem.dsl2.nodes.expressions.operators;

import fr.jamailun.ultimatespellsystem.dsl2.errors.SyntaxException;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.type.Type;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.type.TypePrimitive;
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
        boolean canBeAdded = a.is(TypePrimitive.DURATION) || a.is(TypePrimitive.NUMBER);
        if(!canBeAdded) {
            throw new SyntaxException(firstTokenPosition(), "The type " + a + " cannot be added.");
        }
    }

    @Override
    public @NotNull Type getExpressionType() {
        return producedType;
    }
}
