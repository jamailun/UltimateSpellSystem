package fr.jamailun.ultimatespellsystem.runner;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class VariablesSet {

    private final Map<String, Object> objects = new HashMap<>();

    public Object get(String key) {
        return objects.get(key);
    }

    public void set(String key, Object value) {
        if(value == null) {
            objects.remove(key);
            return;
        }
        objects.put(key, value);
    }

    public Optional<Class<?>> getClassOf(String key) {
        if(objects.containsKey(key))
            return Optional.of(objects.get(key).getClass());
        return Optional.empty();
    }

}
