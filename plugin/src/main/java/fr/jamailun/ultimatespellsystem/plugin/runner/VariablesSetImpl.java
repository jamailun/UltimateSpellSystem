package fr.jamailun.ultimatespellsystem.plugin.runner;

import fr.jamailun.ultimatespellsystem.api.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.api.runner.VariablesSet;
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

    private final Map<String, Object> objects = new HashMap<>();

    @Override
    public @NotNull @Unmodifiable List<String> names() {
        return List.copyOf(objects.keySet());
    }

    @Override
    public Object get(@NotNull String key) {
        return objects.get(key);
    }

    @Override
    public void copy(@NotNull VariablesSet parent) {
        parent.names().forEach(n -> objects.put(n, parent.get(n)));
    }

    @Override
    public void set(@NotNull String key, @Nullable Object value) {
        if(value == null) {
            objects.remove(key);
            return;
        }
        objects.put(key, value);
        UltimateSpellSystem.logDebug("§e[Vars] §7["+key+"] <-- " + value);
    }

    @Override
    public @NotNull String toString() {
        return "Vars" + objects;
    }
}
