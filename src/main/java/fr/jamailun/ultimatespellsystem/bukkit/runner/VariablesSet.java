package fr.jamailun.ultimatespellsystem.bukkit.runner;

import fr.jamailun.ultimatespellsystem.bukkit.UltimateSpellSystem;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class VariablesSet {

    private final Map<String, Object> objects = new HashMap<>();

    public Object get(String key) {
        return objects.get(key);
    }

    public void copy(VariablesSet parent) {
        objects.putAll(parent.objects);
    }

    public void set(String key, Object value) {
        if(value == null) {
            objects.remove(key);
            return;
        }
        objects.put(key, value);
        UltimateSpellSystem.logDebug("§e[Vars] §7["+key+"] <-- " + value);
    }

    public Optional<Class<?>> getClassOf(String key) {
        if(objects.containsKey(key))
            return Optional.of(objects.get(key).getClass());
        return Optional.empty();
    }

}
