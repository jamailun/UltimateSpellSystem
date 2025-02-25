package fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.operators;

import fr.jamailun.ultimatespellsystem.dsl.errors.TypeException;
import fr.jamailun.ultimatespellsystem.dsl.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Type;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenPosition;
import org.jetbrains.annotations.NotNull;

public abstract class Operator extends ExpressionNode {

    protected Type producedType;

    protected Operator(TokenPosition position) {
        super(position);
    }

    protected void assertNotMathIncompatible(Type a) {
        switch (a.primitive()) {
            case NULL, BOOLEAN, ENTITY_TYPE, ENTITY, STRING
                    -> throw new TypeException(this, "has type " + getExpressionType() + ". Type incompatible with a math operator.");
            default -> {/* Nothing*/}
        }
    }

    @Override
    public @NotNull Type getExpressionType() {
        return producedType;
    }
}
