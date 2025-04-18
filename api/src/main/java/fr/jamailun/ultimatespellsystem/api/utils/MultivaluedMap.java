package fr.jamailun.ultimatespellsystem.api.utils;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Function;

/**
 * A basic multivalued map implementation. <br/>
 * The implementation uses the {@link HashMap} : check the corresponding documentation for nuances about {@code null} values.
 * @param <K> the key type.
 * @param <V> the value type.
 */
public class MultivaluedMap<K, V> {

    private final Map<K, List<V>> map = new HashMap<>();

    /**
     * Create an empty map.
     */
    public MultivaluedMap() {}

    /**
     * Get all values for the key.
     * @param key the key to use.
     * @return null if the key does not exist.
     */
    public List<V> get(K key) {
        return map.get(key);
    }

    /**
     * Get the first values mapped to the key.
     * @param key the key to use.
     * @return null if the key does not exist.
     */
    public V getFirst(K key) {
        List<V> list = get(key);
        return list == null ? null : list.getFirst();
    }

    /**
     * The amount of keys, not the total amount of elements.
     * @return a non-negative number.
     */
    public int size() {
        return map.size();
    }

    /**
     * Check if at least one value exists.
     * @return true if no value is present in the map.
     */
    public boolean isEmpty() {
        return map.isEmpty();
    }

    /**
     * Add a value.
     * @param key the key to use.
     * @param value the value to use.
     */
    public void put(K key, V value) {
        map.putIfAbsent(key, new ArrayList<>());
        map.get(key).add(value);
    }

    /**
     * Test if a key exists.
     * @param key the key to test.
     * @return true if the key exists.
     */
    public boolean containsKey(K key) {
        return map.containsKey(key);
    }

    /**
     * Add multiple values to the map.
     * @param key the key to use.
     * @param values the values to add.
     */
    public void putAll(K key, @NotNull Collection<V> values) {
        map.putIfAbsent(key, new ArrayList<>());
        map.get(key).addAll(values);
    }

    /**
     * Remove all elements associated to a key.
     * @param key the key to remove.
     */
    public void remove(K key) {
        map.remove(key);
    }

    /**
     * Map all elements of the map to something else. The keys won't be changed.
     * @param mapper the function to map values.
     * @return a new instance.
     * @param <R> the new type of value
     */
    @Contract(pure = true, value = "_ -> _")
    public <R> @NotNull MultivaluedMap<K, R> map(@NotNull Function<V, R> mapper) {
        MultivaluedMap<K, R> output = new MultivaluedMap<>();
        for(Map.Entry<K, List<V>> entry : map.entrySet()) {
            output.putAll(entry.getKey(), entry.getValue().stream().map(mapper).toList());
        }
        return output;
    }

}
