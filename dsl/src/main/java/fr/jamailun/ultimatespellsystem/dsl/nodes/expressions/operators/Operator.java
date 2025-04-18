package fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.operators;

import fr.jamailun.ultimatespellsystem.dsl.errors.TypeException;
import fr.jamailun.ultimatespellsystem.dsl.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Type;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenPosition;
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
        switch (a.primitive()) {
            case NULL, BOOLEAN, ENTITY_TYPE, ENTITY, STRING
                    -> throw new TypeException(this, "has type " + getExpressionType() + ". Type incompatible with a math operator.");
            default -> {/* Nothing*/}
        }
    }

    @Override
    public @NotNull Type getExpressionType() {
        return producedType;
    }
}
