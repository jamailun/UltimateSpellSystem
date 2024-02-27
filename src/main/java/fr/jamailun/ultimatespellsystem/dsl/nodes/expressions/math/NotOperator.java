package fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.math;

import fr.jamailun.ultimatespellsystem.dsl.errors.TypeException;
import fr.jamailun.ultimatespellsystem.dsl.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Type;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.Token;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenPosition;

public class NotOperator extends MonoOperator {
    public NotOperator(Token token, ExpressionNode expression) {
        super(token.pos(), expression);
    }

    @Override
    public MonoOpeType getType() {
        return MonoOpeType.NOT;
    }

    @Override
    public void validateTypes(Type childType) {
        // No collection
        if(childType.isCollection()) {
            throw new TypeException(this, "A NEGATION cannot handle collections.");
        }

        // Only booleans
        if(! childType.is(TypePrimitive.BOOLEAN)) {
            throw new TypeException(this, "A NEGATION can only handle booleans.");
        }
    }

    @Override
    public Type getExpressionType() {
        return TypePrimitive.BOOLEAN.asType();
    }

    @Override
    public String toString() {
        return "NOT(" + child + ")";
    }
}
