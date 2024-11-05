package fr.jamailun.ultimatespellsystem.bukkit.runner.nodes.operators;

import fr.jamailun.ultimatespellsystem.bukkit.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.bukkit.runner.SpellRuntime;
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
            value = list.get(0);
        return evaluate( value);
    }

    /**
     * Internal operation of the operator.
     * @param value the value to handle.
     * @return the returned value of the operator computation.
     */
    protected abstract @NotNull Object evaluate(@NotNull Object value);
}
