package fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.math;

import fr.jamailun.ultimatespellsystem.dsl.errors.TypeException;
import fr.jamailun.ultimatespellsystem.dsl.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Type;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenPosition;

public class SubOperator extends BiOperator {
    public SubOperator(TokenPosition pos, ExpressionNode left, ExpressionNode right) {
        super(pos, left, right);
    }

    @Override
    public BiOpeType getType() {
        return BiOpeType.SUB;
    }

    @Override
    public void validateTypes(Type leftType, Type rightType) {
        // Some types cannot be added at all.
        assertNotMathIncompatible(leftType);
        assertNotMathIncompatible(rightType);

        // No collections !
        if(leftType.isCollection() || rightType.isCollection()) {
            throw new TypeException(this, "A NEGATION cannot handle collections.");
        }

        // Otherwise same type : always compatible
        if(leftType.equals(rightType)) {
            producedType = leftType;
            return;
        }

        throw new TypeException(this, "Incompatibles types for an SUB : " + leftType + " and " + rightType);
    }

    @Override
    public String toString() {
        return left + "-" + right;
    }
}
