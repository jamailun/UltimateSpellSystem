package fr.jamailun.ultimatespellsystem.dsl2.nodes.expressions.operators;

import fr.jamailun.ultimatespellsystem.dsl2.errors.TypeException;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.type.Type;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.type.variables.TypesContext;
import fr.jamailun.ultimatespellsystem.dsl2.tokenization.TokenPosition;
import org.jetbrains.annotations.NotNull;

/**
 * Addition operator.
 */
public class AddOperator extends BiOperator {

    /**
     * New instance.
     * @param pos token position.
     * @param left first operand.
     * @param right second operand
     */
    public AddOperator(TokenPosition pos, ExpressionNode left, ExpressionNode right) {
        super(pos, left, right);
    }

    @Override
    public @NotNull BiOpeType getType() {
        return BiOpeType.ADD;
    }

    @Override
    public void validateTypes(@NotNull Type leftType, @NotNull Type rightType, @NotNull TypesContext context) {
        //FIXME il faut changer tout ça.
        // Les types seront indiqués comme supportant telle ou telle opération.

        // 1) One of them is a String : always compatible
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
