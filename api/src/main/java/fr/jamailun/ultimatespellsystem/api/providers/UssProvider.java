package fr.jamailun.ultimatespellsystem.api.providers;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * An abstract provider for a specific type.
 * @param <T> the provided type.
 */
public abstract class UssProvider<T> {

    private final Map<String, T> data = new HashMap<>();

    /**
     * Normalize an identifier.
     * @param key the key to normalize.
     * @return a non-null normalized string.
     */
    protected @NotNull String prepare(@NotNull String key) {
        return key.toLowerCase().replace(' ', '_');
    }

    /**
     * Internal method, called after registration.
     * @param key the key.
     * @param newElement the newly added element.
     */
    protected void postRegister(@NotNull String key, @NotNull T newElement) {}

    /**
     * Register a new element.
     * @param newElement the new, non-null element to add.
     * @param name the name (non-normalized identifier) of the element.
     * @param nameVariants the accepted non-normalized other names of the element. Each will be implemented as a new entry to the same reference.
     */
    public final void register(@NotNull T newElement, @NotNull String name, String @NotNull ... nameVariants) {
        String pn = prepare(name);
        data.put(pn, newElement);
        postRegister(pn, newElement);

        for(String v : nameVariants) {
            register(newElement, v);
        }
    }

    /**
     * Try to find an element using it's key.
     * @param key the non-null string to use for search. Will be normalized.
     * @return {@code null} if no entry was found.
     */
    public @Nullable T find(@NotNull String key) {
        return data.get(prepare(key));
    }

    /**
     * Try to find an element using it's key.
     * @param key the nullable key to use.
     * @return a non-null optional, empty if key is null or does not identify any element.
     */
    public final @NotNull Optional<T> findOptional(@Nullable String key) {
        if(key == null) return Optional.empty();
        return Optional.ofNullable(find(key));
    }

}
