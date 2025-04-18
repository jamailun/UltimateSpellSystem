package fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.operators;

import fr.jamailun.ultimatespellsystem.dsl.errors.TypeException;
import fr.jamailun.ultimatespellsystem.dsl.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.VariableExpression;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Type;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.variables.TypesContext;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.variables.VariableDefinition;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.variables.VariableReference;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.Token;
import org.jetbrains.annotations.NotNull;

/**
 * A list operator. Can be one of a {@link BiOpeType} for lists.
 */
public class ListOperator extends BiOperator {

    private final BiOpeType opeType;

    public ListOperator(@NotNull Token operand, @NotNull ExpressionNode left, @NotNull ExpressionNode right) {
        super(operand.pos(), left, right);
        opeType = switch (operand.getType()) {
            case LIST_ADD -> BiOpeType.LIST_ADD;
            case LIST_REM -> BiOpeType.LIST_REM;
            case LIST_REM_INDEX -> BiOpeType.LIST_REM_INDEX;
            case LIST_CONTAINS -> BiOpeType.LIST_CONTAINS;
            default -> throw new RuntimeException("Unreachable.");
        };
    }

    @Override
    public @NotNull BiOpeType getType() {
        return opeType;
    }

    @Override
    protected void validateTypes(@NotNull Type leftType, @NotNull Type rightType, @NotNull TypesContext context) {
        // Left must be a list, right cannot be null.
        if(rightType.is(TypePrimitive.NULL))
            throw new TypeException(firstTokenPosition(), "Cannot apply an operator with a NULL-typed right operand (" + right + ")");
        if(! leftType.isCollection())
            throw new TypeException(firstTokenPosition(), "A list operator (" + opeType + ") can ONLY be applied on a LIST for the left operand. Left is " + leftType);

        // We can, eventually, help to figure-out what variable
        if(opeType != BiOpeType.LIST_REM_INDEX) {
            if(left.getExpressionType().is(TypePrimitive.NULL)) {
                helpCompleteVariable(rightType, context);
            } else if(leftType.primitive() != rightType.primitive()) {
                throw new TypeException(firstTokenPosition(), "Cannot have heterogeneous lists. List is " + leftType + ", right operand is " + rightType);
            }
        }

        // if REM_INDEX, right must be numbers
        if(opeType == BiOpeType.LIST_REM_INDEX && ! rightType.is(TypePrimitive.NUMBER))
            throw new TypeException(firstTokenPosition(), "A LIST_REM_INDEX operator can only accept a NUMBER for the right operant.");

        // Produced type is either the left side, either a BOOL for the contains operator.
        if(opeType == BiOpeType.LIST_CONTAINS) {
            producedType = TypePrimitive.BOOLEAN.asType();
        } else {
            producedType = leftType;
        }
    }

    private void helpCompleteVariable(Type rightType, TypesContext context) {
        if(left instanceof VariableExpression varExpr) {
            VariableDefinition var = context.findVariable(varExpr.getVariableName());
            if(var != null) {
                var.register(new VariableReference.Constant(rightType.asCollection(), firstTokenPosition()));
            }
        }
    }

    @Override
    public String toString() {
        return left + " " + opeType + " " + right;
    }
}
