package fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.operators;

import fr.jamailun.ultimatespellsystem.dsl.errors.TypeException;
import fr.jamailun.ultimatespellsystem.dsl.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Type;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenPosition;

public class AddOperator extends BiOperator {
    public AddOperator(TokenPosition pos, ExpressionNode left, ExpressionNode right) {
        super(pos, left, right);
    }

    @Override
    public BiOpeType getType() {
        return BiOpeType.ADD;
    }

    @Override
    public void validateTypes(Type leftType, Type rightType) {
        // 1) One of them is an ADD : always compatible
        if(leftType.is(TypePrimitive.STRING) || rightType.is(TypePrimitive.STRING)) {
            producedType = TypePrimitive.STRING.asType();
            return;
        }

        // 2) Some types cannot be added at all.
        assertNotMathIncompatible(leftType);
        assertNotMathIncompatible(rightType);

        // 3) Same type : always compatible
        if(leftType.equals(rightType)) {
            producedType = leftType;
            return;
        }

        throw new TypeException(this, "Incompatibles types for an ADD : " + leftType + " and " + rightType);
    }

    @Override
    public String toString() {
        return "(" + left + "+" + right + ")";
    }
}
