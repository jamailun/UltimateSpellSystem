package fr.jamailun.ultimatespellsystem.bukkit.runner;

import fr.jamailun.ultimatespellsystem.bukkit.UltimateSpellSystem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * A set of keyed variables.
 */
public final class VariablesSet {

    private final Map<String, Object> objects = new HashMap<>();

    public @NotNull @Unmodifiable List<String> names() {
        return List.copyOf(objects.keySet());
    }

    public Object get(String key) {
        return objects.get(key);
    }

    /**
     * Copy this variables set.
     * @param parent the variables set to clone into this one.
     */
    public void copy(@NotNull VariablesSet parent) {
        objects.putAll(parent.objects);
    }

    /**
     * Set (or unset) a variable.
     * @param key the key of the variable to set or unset.
     * @param value if {@code null}, the variable will be unset. If not-null, it will be set.
     */
    public void set(@NotNull String key, @Nullable Object value) {
        if(value == null) {
            objects.remove(key);
            return;
        }
        objects.put(key, value);
        UltimateSpellSystem.logDebug("§e[Vars] §7["+key+"] <-- " + value);
    }

    public @NotNull Optional<Class<?>> getClassOf(@Nullable String key) {
        if(key != null && objects.containsKey(key))
            return Optional.of(objects.get(key).getClass());
        return Optional.empty();
    }

}
