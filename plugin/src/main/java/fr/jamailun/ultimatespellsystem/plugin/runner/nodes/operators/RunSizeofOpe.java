package fr.jamailun.ultimatespellsystem.plugin.runner.nodes.operators;

import fr.jamailun.ultimatespellsystem.api.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.api.runner.errors.UnreachableRuntimeException;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * Sizeof-operator
 */
public final class RunSizeofOpe extends RuntimeMonoOperator {

    public RunSizeofOpe(@NotNull RuntimeExpression child) {
        super(child);
    }

    @Override
    protected @NotNull Object evaluate(@NotNull Object value) {
        if(value instanceof Collection<?> coll) {
            return coll.size();
        }
        throw new UnreachableRuntimeException("Unexpected type for SIZE_OF-operator : " + value + " | " + value.getClass());
    }

    @Override
    public @NotNull String toString() {
        return "size_of(" + child + ")";
    }
}
