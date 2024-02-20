package fr.jamailun.ultimatespellsystem.dsl.nodes;

import fr.jamailun.ultimatespellsystem.dsl.errors.TypeException;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypesContext;

public abstract class Node {

    public abstract void validateTypes(TypesContext context);

    protected void assertExpressionType(ExpressionNode expression, TypePrimitive type) {
        if(!expression.getExpressionType().is(type))
            throw new TypeException(expression, type);
    }

    protected void assertExpressionType(ExpressionNode expression, TypePrimitive type, TypesContext context) {
        expression.validateTypes(context);
        if(!expression.getExpressionType().is(type))
            throw new TypeException(expression, type);
    }

}
