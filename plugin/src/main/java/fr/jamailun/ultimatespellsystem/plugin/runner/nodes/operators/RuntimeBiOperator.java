package fr.jamailun.ultimatespellsystem.plugin.runner.nodes.operators;

import fr.jamailun.ultimatespellsystem.api.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.api.runner.SpellRuntime;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * A bi-operator for the runtime.
 */
public abstract class RuntimeBiOperator extends RuntimeExpression {

    protected final RuntimeExpression leftExpression;
    protected final RuntimeExpression rightExpression;

    public RuntimeBiOperator(@NotNull RuntimeExpression left, @NotNull RuntimeExpression right) {
        this.leftExpression = left;
        this.rightExpression = right;
    }

    @Override
    public final @NotNull Object evaluate(@NotNull SpellRuntime runtime) {
        Object left = leftExpression.evaluate(runtime);
        if(left instanceof List<?> listLeft && listLeft.size() == 1)
            left = listLeft.get(0);

        Object right = rightExpression.evaluate(runtime);
        if(right instanceof List<?> listRight && listRight.size() == 1)
            right = listRight.get(0);

        return evaluate(left, right);
    }

    protected abstract Object evaluate(Object left, Object right);
}
