package fr.jamailun.ultimatespellsystem.bukkit.runner.nodes.operators;

import fr.jamailun.ultimatespellsystem.bukkit.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.bukkit.runner.errors.UnreachableRuntimeException;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Duration;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Not-operator
 */
public final class RunNotOpe extends RuntimeMonoOperator {

    public RunNotOpe(@NotNull RuntimeExpression child) {
        super(child);
    }

    @Override
    @SuppressWarnings("unchecked")
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
