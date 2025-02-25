package fr.jamailun.ultimatespellsystem.plugin.runner.nodes.operators;

import fr.jamailun.ultimatespellsystem.api.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.api.runner.SpellRuntime;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * A mono-operator for the runtime environment.
 */
@RequiredArgsConstructor
public abstract class RuntimeMonoOperator extends RuntimeExpression {

    protected final RuntimeExpression child;

    @Override
    public final @NotNull Object evaluate(@NotNull SpellRuntime runtime) {
        Object value = child.evaluate(runtime);
        if(child instanceof List<?> list && list.size() == 1)
            value = list.getFirst();
        return evaluate( value);
    }

    /**
     * Internal operation of the operator.
     * @param value the value to handle.
     * @return the returned value of the operator computation.
     */
    protected abstract @NotNull Object evaluate(@NotNull Object value);
}
