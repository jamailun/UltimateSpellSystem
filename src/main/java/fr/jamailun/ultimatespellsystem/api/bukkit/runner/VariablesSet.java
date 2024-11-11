package fr.jamailun.ultimatespellsystem.api.bukkit.runner;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collection;

/**
 * A set of keyed variables.
 */
public interface VariablesSet {

    /**
     * Unmodifiable list of the variables names.
     * @return a non-null, unmodifiable collection of strings.
     */
    @NotNull @Unmodifiable
    Collection<String> names();

    /**
     * Get a value.
     * @param key the variable key.
     * @return the content of the variable, or {@code null} if not defined.
     */
    Object get(@NotNull String key);

    /**
     * Copy this variables set.
     * @param parent the variables set to clone into this one.
     */
    void copy(@NotNull VariablesSet parent);

    /**
     * Set (or unset) a variable.
     * @param key the key of the variable to set or unset.
     * @param value if {@code null}, the variable will be unset. If not-null, it will be set.
     */
    void set(@NotNull String key, @Nullable Object value);

}
