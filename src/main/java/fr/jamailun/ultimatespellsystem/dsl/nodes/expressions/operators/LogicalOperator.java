package fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.operators;

import fr.jamailun.ultimatespellsystem.dsl.errors.TypeException;
import fr.jamailun.ultimatespellsystem.dsl.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Type;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.Token;

public class LogicalOperator extends BiOperator {

    private final BiOpeType type;

    public LogicalOperator(Token operand, ExpressionNode left, ExpressionNode right) {
        super(operand.pos(), left, right);
        this.type = switch (operand.getType()) {
            case COMP_EQ -> BiOpeType.EQUAL;
            case COMP_NE -> BiOpeType.NOT_EQUAL;
            case COMP_LE -> BiOpeType.LESSER_OR_EQ;
            case COMP_LT -> BiOpeType.LESSER;
            case COMP_GE -> BiOpeType.GREATER_OR_EQ;
            case COMP_GT -> BiOpeType.GREATER;
            case OPE_AND -> BiOpeType.AND;
            case OPE_OR -> BiOpeType.OR;
            default -> throw new RuntimeException("Invalid logical operator : " + operand + " at " + operand.pos());
        };
    }

    @Override
    public void validateTypes(Type leftType, Type rightType) {
        // Do not allow collections
        if(leftType.isCollection() || rightType.isCollection()) {
            throw new TypeException(this, "A " + type + " cannot handle collections.");
        }

        // Only allow same type
        if(leftType.primitive() != rightType.primitive()) {
            throw new TypeException("Logical operator "+this+" has unequal types : "+leftType+ " and "+rightType+".");
        }

        // If inequality, must be numeric
        if(type == BiOpeType.GREATER || type == BiOpeType.GREATER_OR_EQ || type == BiOpeType.LESSER || type == BiOpeType.LESSER_OR_EQ) {
            if(leftType.primitive() != TypePrimitive.NUMBER && leftType.primitive() != TypePrimitive.DURATION) {
                throw new TypeException(this, "A comparison can only compare numeric values !");
            }
        }
    }

    @Override
    public BiOpeType getType() {
        return type;
    }

    @Override
    public Type getExpressionType() {
        return TypePrimitive.BOOLEAN.asType();
    }

    @Override
    public String toString() {
        return  "(" + left + " " + type + " " + right + ")";
    }
}
