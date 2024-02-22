package fr.jamailun.ultimatespellsystem.runner;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

import java.util.HashMap;
import java.util.Map;

public class VariablesSet {

    private final Map<String, LivingEntity> entities = new HashMap<>();
    private final Map<String, Boolean> booleans = new HashMap<>();
    private final Map<String, Double> numbers = new HashMap<>();
    private final Map<String, String> strings = new HashMap<>();
    private final Map<String, EntityType> entityTypes = new HashMap<>();

    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> clazz) {
        if(clazz.isAssignableFrom(LivingEntity.class))
            return (T) entities.get(key);
        if(String.class.equals(clazz))
            return (T) strings.get(key);
        if(Double.class.equals(clazz))
            return (T) numbers.get(key);
        if(Boolean.class.equals(clazz))
            return (T) booleans.get(key);
        if(EntityType.class.equals(clazz))
            return (T) entityTypes.get(key);
        throw new RuntimeException("Invalid type : " + clazz);
    }

    public void set(String key, Object value) {
        if(value == null)
            return;

        if(value instanceof LivingEntity v) {
            entities.put(key, v);
            return;
        }
        if(value instanceof String v) {
            strings.put(key, v);
            return;
        }
        if(value instanceof Double v) {
            numbers.put(key, v);
            return;
        }
        if(value instanceof Boolean v) {
            booleans.put(key, v);
            return;
        }
        if(value instanceof EntityType v) {
            entityTypes.put(key, v);
            return;
        }
        throw new RuntimeException("Invalid type : " + value.getClass());
    }


}
