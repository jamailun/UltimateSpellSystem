package fr.jamailun.ultimatespellsystem.dsl2.nodes.expressions;

import fr.jamailun.ultimatespellsystem.dsl2.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.type.Type;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.type.variables.TypesContext;
import fr.jamailun.ultimatespellsystem.dsl2.visitor.ExpressionVisitor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

/**
 * Get array value at index.
 */
@Getter
public class FieldGetExpression extends ExpressionNode {

    private final ExpressionNode leftExpression;
    private final String fieldName;

    public FieldGetExpression(@NotNull ExpressionNode leftExpression, @NotNull String fieldName) {
        super(leftExpression.firstTokenPosition());
        this.leftExpression = leftExpression;
        this.fieldName = fieldName;
    }

    @Override
    public @NotNull Type getExpressionType() {
        //TODO pas facile, il faut les définitions des structures.
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void validateTypes(@NotNull TypesContext context) {
        //TODO
    }

    @Override
    public void visit(@NotNull ExpressionVisitor visitor) {
        visitor.handleFieldGet(this);
    }

    @Override
    public String toString() {
        return firstTokenPosition() + "." + fieldName;
    }
}
