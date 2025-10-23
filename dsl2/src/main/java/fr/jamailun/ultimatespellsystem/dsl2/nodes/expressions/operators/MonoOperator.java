package fr.jamailun.ultimatespellsystem.dsl2.nodes.expressions.operators;

import fr.jamailun.ultimatespellsystem.dsl2.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.type.Type;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.type.variables.TypesContext;
import fr.jamailun.ultimatespellsystem.dsl2.tokenization.TokenPosition;
import fr.jamailun.ultimatespellsystem.dsl2.visitor.ExpressionVisitor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.function.Function;

/**
 * Mono-operator, on a single expression.
 */
@Getter
public abstract class MonoOperator extends Operator {

    protected final ExpressionNode child;

    protected MonoOperator(TokenPosition position, ExpressionNode child) {
        super(position);
        this.child = child;
    }

    /**
     * Get this instance type of operation.
     * @return a non-null type of mono-operation.
     */
    public abstract @NotNull MonoOpeType getType();

    @Override
    public void visit(@NotNull ExpressionVisitor visitor) {
        visitor.handleMonoOperator(this);
    }

    @Override
    public final void validateTypes(@NotNull TypesContext context) {
        child.validateTypes(context);

       validateTypes(child.getExpressionType());
    }

    /**
     * Validate the static-type of the code usage.
     * @param childType the type to test.
     */
    public abstract void validateTypes(@NotNull Type childType);

    public enum MonoOpeType {
        NOT;
    }

}
