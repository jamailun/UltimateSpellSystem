package fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.math;

import fr.jamailun.ultimatespellsystem.dsl.errors.TypeException;
import fr.jamailun.ultimatespellsystem.dsl.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Type;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenPosition;

public class MathFunctionOperator extends MonoOperator {

    private final MonoOpeType type;

    public MathFunctionOperator(TokenPosition pos, ExpressionNode expression, MonoOpeType type) {
        super(pos, expression);
        this.type = type;
    }

    @Override
    public MonoOpeType getType() {
        return type;
    }

    @Override
    public void validateTypes(Type childType) {
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
    public Type getExpressionType() {
        return TypePrimitive.NUMBER.asType();
    }

    @Override
    public String toString() {
        return type + "(" + child + ")";
    }
}
