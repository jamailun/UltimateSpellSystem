package fr.jamailun.ultimatespellsystem.api.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

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

    /**
     * Parse a map, and read mono and multi elements.
     * @param key the key of the map to read.
     * @param builder a function to transform a Map into a new element.
     * @return a new, non-null collection of parsed elements.
     * @param <R> the produced type.
     */
    default <R> @NotNull Collection<R> parseMap(@NotNull String key, @NotNull BiFunction<Map<?,?>, String, R> builder) {
        List<R> output = new ArrayList<>();

        // Read mono
        Map<?,?> monoMap = tryGetAttribute(key, Map.class);
        if(monoMap != null) {
            R element = builder.apply(monoMap, key);
            if(element != null)
                output.add(element);
        }

        // Read multi
        String keyMulti = key + "s";
        if(hasAttribute(keyMulti)) {
            for(Map<?,?> map : tryGetAttributes(keyMulti, Map.class)) {
                R element = builder.apply(map, keyMulti);
                if(element != null) {
                    output.add(element);
                }
            }
        }

        return output;
    }

    /**
     * Parse an element of the map.
     * @param key the key to use.
     * @param builder a function to transform a map of data into a new element.
     * @return null if the element is not null (or if the {@code builder} returns null).
     * @param <R> the produced type.
     */
    default <R> @Nullable R parseMap(@NotNull String key, @NotNull Function<Map<?,?>, R> builder) {
        Map<?,?> map = tryGetAttribute(key, Map.class);
        if(map != null) {
            return builder.apply(map);
        }
        return null;
    }

    /**
     * Try to get an int.
     * @param key the key to use.
     * @param defaultValue default value to return if the key does not exist.
     * @return either the rounded value, or the default value.
     */
    default int tryGetInt(@NotNull String key, int defaultValue) {
        return tryGetAttribute(key, Double.class, (double)defaultValue).intValue();
    }

}
