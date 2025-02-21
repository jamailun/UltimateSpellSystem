package fr.jamailun.ultimatespellsystem.api.spells;

import java.util.List;

/**
 * A meta-data container for the spell.
 */
public interface SpellMetadata {

    /**
     * Test if the metadata has parameters.
     * @return true if no parameter exist.
     */
    boolean isEmpty();

    /**
     * Get the amount of parameters.
     * @return a non-negative integer.
     */
    int size();

    /**
     * Get the first parameter, evaluated.
     * @param clazz the class to evaluate the value into.
     * @return null if class is not assignable.
     * @param <T> the type to get.
     */
    default <T> T getFirst(Class<T> clazz) {
        return get(0, clazz);
    }

    /**
     * Get an evaluated parameter.
     * @param index the index of the parameter. If OOB, returns null.
     * @param clazz the class to evaluate the value into.
     * @return null if class is not assignable.
     * @param <T> the type to get.
     */
    <T> T get(int index, Class<T> clazz);

    /**
     * Get all evaluated parameters.
     * @param clazz the class to evaluate the values into.
     * @return a new, non-null list.
     * @param <T> the type to get.
     */
    <T> List<T> get(Class<T> clazz);

}
