package fr.jamailun.ultimatespellsystem.api.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * Can hold attributes.
 */
public interface AttributesHolder {

    /**
     * Get a specific attribute.
     * @param key the key of the attribute.
     * @return null if the key is not set.
     */
    @Nullable Object getAttribute(@NotNull String key);

    /**
     * Try to get a custom attribute.
     * @param key the key of the attribute.
     * @param clazz the class to cast the attribute to.
     * @return the default value OR a non-null value of required type.
     * @param <T> the generic to use, for the returned value.
     */
    <T> @Nullable T tryGetAttribute(@NotNull String key, @NotNull Class<T> clazz);

    /**
     * Try to get a custom attribute.
     * @param key the key of the attribute.
     * @param clazz the class to cast the attribute to.
     * @param defaultValue the default value. Returned if the key is not set, <b>or</b> if the cast fails.
     * @return the default value OR a non-null value of required type.
     * @param <T> the generic to use, for the returned value.
     */
    <T> @NotNull T tryGetAttribute(@NotNull String key, @NotNull Class<T> clazz, @NotNull T defaultValue);

    /**
     * Test if an attribute exists.
     * @param key the key to test.
     * @return true if some attribute exists with this key.
     */
    boolean hasAttribute(@NotNull String key);

    /**
     * Try to get a custom attribute, as a list.
     * @param key the key of the attribute.
     * @param clazz the class to cast the attribute to.
     * @return a list of attributes, <b>or</b> an empty-list if attribute is ot-found or if an error occurs.
     * @param <R> the generic to use, for the returned value.
     */
    <R> @NotNull List<R> tryGetAttributes(@NotNull String key, @NotNull Class<R> clazz);

}
