package fr.jamailun.ultimatespellsystem.plugin.runner;

import fr.jamailun.ultimatespellsystem.UssLogger;
import fr.jamailun.ultimatespellsystem.api.runner.VariablesSet;
import fr.jamailun.ultimatespellsystem.api.runner.errors.InvalidTypeException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A set of keyed variables.
 */
public final class VariablesSetImpl implements VariablesSet {

    private final Map<String, VarEntry> variables = new HashMap<>();

    @Override
    public @NotNull @Unmodifiable List<String> names() {
        return List.copyOf(variables.keySet());
    }

    @Override
    public @Nullable Object get(@NotNull String key) {
        if(variables.containsKey(key)) {
            return variables.get(key).content;
        }
        return null;
    }

    @Override
    public <T> @Nullable T get(@NotNull String key, @NotNull Class<T> clazz) {
        Object raw = get(key);
        try {
            return clazz.cast(raw);
        } catch(ClassCastException e) {
            throw new InvalidTypeException("Get variable '" + key + "'", clazz.getSimpleName(), raw);
        }
    }

    @Override
    public @NotNull VariablesSetImpl inherit() {
        VariablesSetImpl output = new VariablesSetImpl();
        // We propagate REFERENCES to our objects.
        // This allows inner blocks to mutate variables.
        output.variables.putAll(variables);
        return output;
    }

    @Override
    public void set(@NotNull String key, @Nullable Object value) {
        variables.putIfAbsent(key, new VarEntry());
        variables.get(key).content = value;
        UssLogger.logDebug("§e[Vars] §7%"+key+" <-- " + value);
    }

    @Override
    public @NotNull String toString() {
        return variables.toString();
    }

    private static class VarEntry {
        private Object content;
    }
}
