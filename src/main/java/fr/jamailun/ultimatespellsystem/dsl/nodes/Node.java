package fr.jamailun.ultimatespellsystem.dsl.nodes;

import fr.jamailun.ultimatespellsystem.dsl.errors.TypeException;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypesContext;

import java.util.ArrayList;
import java.util.List;

/**
 * A node is an element of the AST.
 * It can be a statement of an expression.
 */
public abstract class Node {

    /**
     * Validates the TYPE of the handled elements. No logic test is done here.
     * @param context the typing context.
     */
    public abstract void validateTypes(TypesContext context);

    /**
     * Assert an expression to be of a specific type.
     * @param expression the expression to check.
     * @param type the expected type.
     * @param otherTypes a variadic for other allowed types.
     */
    protected void assertExpressionType(ExpressionNode expression, TypePrimitive type, TypePrimitive... otherTypes) {
        List<TypePrimitive> allowed = new ArrayList<>(List.of(otherTypes));
        allowed.add(type);

        if(!allowed.contains(expression.getExpressionType().primitive()))
            throw new TypeException(expression, type);
    }

    /**
     * Validate the type of expression, and then it to be of a specific type.
     * @param expression the expression to check.
     * @param context the current context.
     * @param type the expected type.
     * @param otherTypes a variadic for other allowed types.
     */
    protected void assertExpressionType(ExpressionNode expression, TypesContext context, TypePrimitive type, TypePrimitive... otherTypes) {
        expression.validateTypes(context);
        assertExpressionType(expression, type, otherTypes);
    }

}
