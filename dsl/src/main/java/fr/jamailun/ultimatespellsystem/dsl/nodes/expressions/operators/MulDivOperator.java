package fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.operators;

import fr.jamailun.ultimatespellsystem.dsl.errors.TypeException;
import fr.jamailun.ultimatespellsystem.dsl.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Type;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.variables.TypesContext;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.Token;
import org.jetbrains.annotations.NotNull;

/**
 * Multiplication or division.
 */
public class MulDivOperator extends BiOperator {

    private final BiOpeType type;

    /**
     * New instance.
     * @param operatorToken token to use.
     * @param left first operand.
     * @param right second operand
     */
    public MulDivOperator(Token operatorToken, ExpressionNode left, ExpressionNode right) {
        super(operatorToken.pos(), left, right);
        type = switch (operatorToken.getType()) {
            case OPE_MUL -> BiOpeType.MUL;
            case OPE_DIV -> BiOpeType.DIV;
            default -> throw new RuntimeException("Cannot create MulDivOperator with an operand: " + operatorToken + operatorToken.pos());
        };
    }

    @Override
    public @NotNull BiOpeType getType() {
        return type;
    }

    @Override
    public void validateTypes(@NotNull Type leftType, @NotNull Type rightType, @NotNull TypesContext context) {
        // 1) Do not allow collections
        if(leftType.isCollection() || rightType.isCollection()) {
            throw new TypeException(this, "A "+type+" cannot handle collections.");
        }

        // 2.a) Allow duration/duration
        if(leftType.is(TypePrimitive.DURATION) && rightType.is(TypePrimitive.DURATION)) {
            producedType = TypePrimitive.NUMBER.asType();
            return;
        }

        // 2.b) Only allow number on the right.
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
