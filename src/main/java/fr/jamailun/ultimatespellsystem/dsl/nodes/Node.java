package fr.jamailun.ultimatespellsystem.dsl.nodes;

import fr.jamailun.ultimatespellsystem.dsl.errors.TypeException;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypesContext;

import java.util.ArrayList;
import java.util.List;

public abstract class Node {

    public abstract void validateTypes(TypesContext context);

    protected void assertExpressionType(ExpressionNode expression, TypePrimitive type, TypePrimitive... otherTypes) {
        List<TypePrimitive> allowed = new ArrayList<>(List.of(otherTypes));
        allowed.add(type);

        if(!allowed.contains(expression.getExpressionType().primitive()))
            throw new TypeException(expression, type);
    }

    protected void assertExpressionType(ExpressionNode expression, TypesContext context, TypePrimitive type, TypePrimitive... otherTypes) {
        expression.validateTypes(context);
        assertExpressionType(expression, type, otherTypes);
    }

}
