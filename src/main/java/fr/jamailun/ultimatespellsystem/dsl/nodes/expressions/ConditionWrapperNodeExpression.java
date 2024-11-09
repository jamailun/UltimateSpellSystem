package fr.jamailun.ultimatespellsystem.dsl.nodes.expressions;

import fr.jamailun.ultimatespellsystem.dsl.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Type;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypesContext;
import fr.jamailun.ultimatespellsystem.dsl.visitor.ExpressionVisitor;
import org.jetbrains.annotations.NotNull;

/**
 * Type validation only ! used to force values to be a boolean !
 */
public class ConditionWrapperNodeExpression extends ExpressionNode {

    private final ExpressionNode child;

    public ConditionWrapperNodeExpression(ExpressionNode child) {
        super(child.firstTokenPosition());
        this.child = child;
    }

    @Override
    public @NotNull Type getExpressionType() {
        return TypePrimitive.BOOLEAN.asType();
    }

    @Override
    public void validateTypes(@NotNull TypesContext context) {
        System.out.println("Validating " + child);
        assertExpressionType(child, context, TypePrimitive.BOOLEAN);
    }

    @Override
    public String toString() {
        return ":" + child;
    }

    @Override
    public void visit(@NotNull ExpressionVisitor visitor) {
        child.visit(visitor);
    }
}
