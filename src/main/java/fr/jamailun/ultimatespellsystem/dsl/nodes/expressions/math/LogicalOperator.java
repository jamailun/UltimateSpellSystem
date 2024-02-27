package fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.math;

import fr.jamailun.ultimatespellsystem.dsl.errors.TypeException;
import fr.jamailun.ultimatespellsystem.dsl.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Type;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenPosition;

public class LogicalOperator extends BiOperator {

    private final BiOpeType type;

    public LogicalOperator(TokenPosition position, ExpressionNode left, ExpressionNode right, BiOpeType type) {
        super(position, left, right);
        this.type = type;
    }

    @Override
    public void validateTypes(Type leftType, Type rightType) {
        // 1) Do not allow collections
        if(leftType.isCollection() || rightType.isCollection()) {
            throw new TypeException(this, "A "+type+" cannot handle collections.");
        }
        // Donot accept collections.

        // EQ / NOT-EQ : MUST be same type
    }

    @Override
    public BiOpeType getType() {
        return type;
    }

    @Override
    public String toString() {
        return  "(" + left + " " + type + " " + right + ")";
    }
}
