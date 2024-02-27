package fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.math;

import fr.jamailun.ultimatespellsystem.dsl.errors.TypeException;
import fr.jamailun.ultimatespellsystem.dsl.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Type;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.Token;

public class MulDivOperator extends BiOperator {

    private final BiOpeType type;

    public MulDivOperator(Token operand, ExpressionNode left, ExpressionNode right) {
        super(operand.pos(), left, right);
        type = switch (operand.getType()) {
            case OPE_MUL -> BiOpeType.MUL;
            case OPE_DIV -> BiOpeType.DIV;
            default -> throw new RuntimeException("Cannot create MulDivOperator with an operand: " + operand + operand.pos());
        };
    }

    @Override
    public BiOpeType getType() {
        return type;
    }

    @Override
    public void validateTypes(Type leftType, Type rightType) {
        // 1) Do not allow collections
        if(leftType.isCollection() || rightType.isCollection()) {
            throw new TypeException(this, "A "+type+" cannot handle collections.");
        }

        // 2) Only allow number on the right.
        if(! rightType.is(TypePrimitive.NUMBER)) {
            throw new TypeException(this, "Right expression is of type " + rightType + " : must be a number for "+type+" operand.");
        }

        // 3) Allow : (DURATION/LOCATION/NUMBER) with (NUMBER)
        if(leftType.is(TypePrimitive.DURATION) || leftType.is(TypePrimitive.LOCATION) || leftType.is(TypePrimitive.NUMBER)) {
            producedType = leftType;
            return;
        }

        throw new TypeException(this, "Incompatibles types for an SUB : " + leftType + " and " + rightType);
    }

    @Override
    public String toString() {
        return "(" + left + (type==BiOpeType.MUL?"*":"/") + right + ")";
    }
}
