package fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.operators;

import fr.jamailun.ultimatespellsystem.dsl.errors.TypeException;
import fr.jamailun.ultimatespellsystem.dsl.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Type;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.Token;
import org.jetbrains.annotations.NotNull;

/**
 * A NOT Operator for a boolean child-expression.
 */
public class NotOperator extends MonoOperator {
    public NotOperator(@NotNull Token token, ExpressionNode expression) {
        super(token.pos(), expression);
    }

    @Override
    public @NotNull MonoOpeType getType() {
        return MonoOpeType.NOT;
    }

    @Override
    public void validateTypes(@NotNull Type childType) {
        // No collection
        if(childType.isCollection()) {
            throw new TypeException(this, "A NEGATION cannot handle collections.");
        }

        // Only booleans
        if(! childType.is(TypePrimitive.BOOLEAN)) {
            throw new TypeException(this, "A NEGATION can only handle booleans.");
        }
    }

    @Override
    public @NotNull Type getExpressionType() {
        return TypePrimitive.BOOLEAN.asType();
    }

    @Override
    public String toString() {
        return "NOT(" + child + ")";
    }
}
