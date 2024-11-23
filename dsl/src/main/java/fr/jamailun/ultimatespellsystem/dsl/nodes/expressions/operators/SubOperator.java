package fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.operators;

import fr.jamailun.ultimatespellsystem.dsl.errors.TypeException;
import fr.jamailun.ultimatespellsystem.dsl.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Type;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenPosition;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SubOperator extends BiOperator {
    public SubOperator(TokenPosition pos, ExpressionNode left, ExpressionNode right) {
        super(pos, left, right);
    }

    @Override
    public @NotNull BiOpeType getType() {
        return BiOpeType.SUB;
    }

    private final static List<TypePrimitive> ALLOWED = List.of(TypePrimitive.NUMBER, TypePrimitive.DURATION);

    @Override
    public void validateTypes(Type leftType, Type rightType) {
        // No collections !
        if(leftType.isCollection() || rightType.isCollection()) {
            throw new TypeException(this, "A NEGATION cannot handle collections.");
        }

        if(!ALLOWED.contains(leftType.primitive()))
            throw new TypeException(this, "SUB cannot handle " + leftType);
        if(!ALLOWED.contains(rightType.primitive()))
            throw new TypeException(this, "SUB cannot handle " + rightType);


        // Otherwise same type : always compatible
        if(leftType.primitive() == rightType.primitive()) {
            producedType = leftType;
            return;
        }

        throw new TypeException(this, "Incompatibles types for an SUB : " + leftType + " and " + rightType);
    }

    @Override
    public String toString() {
        return "(" + left + "-" + right + ")";
    }
}
