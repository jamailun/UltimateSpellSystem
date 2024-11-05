package fr.jamailun.ultimatespellsystem.dsl.nodes.expressions;

import fr.jamailun.ultimatespellsystem.dsl.errors.TypeException;
import fr.jamailun.ultimatespellsystem.dsl.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.CollectionFilter;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Type;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypesContext;
import fr.jamailun.ultimatespellsystem.dsl.visitor.ExpressionVisitor;
import org.jetbrains.annotations.NotNull;

public class ArrayGetterExpression extends ExpressionNode {

    private final ExpressionNode array;
    private final ExpressionNode index;

    public ArrayGetterExpression(ExpressionNode array, ExpressionNode index) {
        super(array.firstTokenPosition());
        this.array = array;
        this.index = index;
    }

    @Override
    public @NotNull Type getExpressionType() {
        return array.getExpressionType().asMonoElement();
    }

    @Override
    public void validateTypes(TypesContext context) {
        // Index must be a number
        assertExpressionType(index, CollectionFilter.MONO_ELEMENT, context, TypePrimitive.NUMBER);

        // Array must be a collection
        array.validateTypes(context);
        Type typeArray = array.getExpressionType();
        if( ! typeArray.is(TypePrimitive.NULL) && ! typeArray.isCollection()) {
            throw new TypeException(this, "Cannot get the value of a non-array.");
        }
    }

    public ExpressionNode getArray() {
        return array;
    }

    public ExpressionNode getIndex() {
        return index;
    }

    @Override
    public void visit(@NotNull ExpressionVisitor visitor) {
        visitor.handleArrayGet(this);
    }

    @Override
    public String toString() {
        return array + "[" + index + "]";
    }
}
