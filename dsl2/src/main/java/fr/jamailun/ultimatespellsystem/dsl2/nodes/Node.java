package fr.jamailun.ultimatespellsystem.dsl2.nodes;

import fr.jamailun.ultimatespellsystem.dsl2.errors.TypeException;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.type.CollectionFilter;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.type.Type;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.type.variables.TypesContext;
import org.jetbrains.annotations.NotNull;

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
    public abstract void validateTypes(@NotNull TypesContext context);

    /**
     * Assert an expression to be of a specific type.
     * @param expression the expression to check.
     * @param filter the filter for collection.
     * @param type the expected type.
     * @param otherTypes a variadic for other allowed types.
     */
    protected void assertExpressionType(@NotNull ExpressionNode expression, @NotNull CollectionFilter filter, @NotNull TypePrimitive type, TypePrimitive... otherTypes) {
        if(expression.getExpressionType().is(TypePrimitive.NULL))
            return; // Ignore NULL type : it is accepted by everything.

        List<TypePrimitive> allowed = new ArrayList<>(List.of(otherTypes));
        allowed.add(type);

        if(! expression.getExpressionType().isOneOf(allowed.toArray(new TypePrimitive[0])))
            throw new TypeException(expression, type);

        if(!filter.isValid(expression.getExpressionType())) {
            throw new TypeException(expression, "Type is correct, but expected a "+filter+".");
        }
    }

    protected void assertExpressionType(@NotNull ExpressionNode expression, @NotNull Type type) {
        if(type.is(TypePrimitive.NULL))
            return; // On ignore tlr type null

        if(!expression.getExpressionType().equals(type))
            throw new TypeException(expression, "Type is correct, but expected a "+type+".");
    }

    /**
     * Validate the type of expression, and then it to be of a specific type.
     * @param expression the expression to check.
     * @param filter the filter for collection.
     * @param context the current context.
     * @param type the expected type.
     * @param otherTypes a variadic for other allowed types.
     */
    protected void assertExpressionType(@NotNull ExpressionNode expression, @NotNull CollectionFilter filter, @NotNull TypesContext context, @NotNull TypePrimitive type, TypePrimitive... otherTypes) {
        expression.validateTypes(context);
        assertExpressionType(expression, filter, type, otherTypes);
    }

    /**
     * Validate the type of expression, and then it to be of a specific type.
     * @param expression the expression to check.
     * @param context the current context.
     * @param type the expected type.
     * @param otherTypes a variadic for other allowed types.
     */
    protected void assertExpressionType(@NotNull ExpressionNode expression, @NotNull TypesContext context, @NotNull TypePrimitive type, TypePrimitive... otherTypes) {
        assertExpressionType(expression, CollectionFilter.ANY, context, type, otherTypes);
    }

}
