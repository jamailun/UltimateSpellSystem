package fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.operators;

import fr.jamailun.ultimatespellsystem.dsl.errors.TypeException;
import fr.jamailun.ultimatespellsystem.dsl.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Type;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenPosition;
import org.jetbrains.annotations.NotNull;

/**
 * Math-function mono-operator.
 */
public class MathFunctionOperator extends MonoOperator {

    private final MonoOpeType type;

    /**
     * New instance.
     * @param pos position of the token.
     * @param expression expression to use.
     * @param type type of operator.
     */
    public MathFunctionOperator(@NotNull TokenPosition pos, @NotNull ExpressionNode expression, @NotNull MonoOpeType type) {
        super(pos, expression);
        this.type = type;
    }

    @Override
    public @NotNull MonoOpeType getType() {
        return type;
    }

    @Override
    public void validateTypes(@NotNull Type childType) {
        // No collection
        if(childType.isCollection()) {
            throw new TypeException(this, "A MathOperation("+type+") cannot handle collections.");
        }

        // Only numbers
        if(! childType.is(TypePrimitive.NUMBER)) {
            throw new TypeException(this, "A MathOperation("+type+") can only handle numbers.");
        }
    }

    @Override
    public @NotNull Type getExpressionType() {
        return TypePrimitive.NUMBER.asType();
    }

    @Override
    public String toString() {
        return type + "(" + child + ")";
    }
}
