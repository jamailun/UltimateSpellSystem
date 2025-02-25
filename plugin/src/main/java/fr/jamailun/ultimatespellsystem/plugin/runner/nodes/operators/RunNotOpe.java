package fr.jamailun.ultimatespellsystem.plugin.runner.nodes.operators;

import fr.jamailun.ultimatespellsystem.api.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.api.runner.errors.UnreachableRuntimeException;
import org.jetbrains.annotations.NotNull;

/**
 * Not-operator
 */
public final class RunNotOpe extends RuntimeMonoOperator {

    public RunNotOpe(@NotNull RuntimeExpression child) {
        super(child);
    }

    @Override
    protected @NotNull Object evaluate(@NotNull Object value) {
        // Anyone of them is a String
        if(value instanceof Boolean b) {
            return ! b;
        }
        throw new UnreachableRuntimeException("Unexpected type for NOT-operator : " + value + " | " + value.getClass());
    }

    @Override
    public @NotNull String toString() {
        return "not(" + child + ")";
    }
}
