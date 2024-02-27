package fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.math;

import fr.jamailun.ultimatespellsystem.dsl.errors.TypeException;
import fr.jamailun.ultimatespellsystem.dsl.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Type;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenPosition;

public abstract class Operator extends ExpressionNode {

    protected Type producedType;

    protected Operator(TokenPosition position) {
        super(position);
    }

    protected void assertNotMathIncompatible(Type a) {
        switch (a.primitive()) {
            case NULL, BOOLEAN, MATERIAL, EFFECT_TYPE, ENTITY_TYPE, ENTITY, STRING
                    -> throw new TypeException(this, "has type " + getExpressionType() + ". Type incompatible with a math operator.");
            default -> {/* Nothing*/}
        }
    }

    @Override
    public Type getExpressionType() {
        return producedType;
    }
}
