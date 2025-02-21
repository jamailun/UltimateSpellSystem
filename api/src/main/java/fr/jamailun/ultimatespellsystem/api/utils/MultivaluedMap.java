package fr.jamailun.ultimatespellsystem.api.utils;

import java.util.*;

public class MultivaluedMap<K, V> {

    private final Map<K, List<V>> map = new HashMap<>();

    public List<V> get(K key) {
        return map.get(key);
    }

    public V getFirst(K key) {
        List<V> list = get(key);
        return list == null ? null : list.getFirst();
    }

    public int size() {
        return map.size();
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    public void put(K key, V value) {
        map.putIfAbsent(key, new ArrayList<>());
        map.get(key).add(value);
    }

    public boolean containsKey(K key) {
        return map.containsKey(key);
    }

    public void putAll(K key, Collection<V> values) {
        map.putIfAbsent(key, new ArrayList<>());
        map.get(key).addAll(values);
    }

    public void remove(K key) {
        map.remove(key);
    }

}
