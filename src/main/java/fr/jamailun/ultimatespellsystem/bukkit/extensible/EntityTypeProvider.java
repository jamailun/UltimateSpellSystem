package fr.jamailun.ultimatespellsystem.bukkit.extensible;

import fr.jamailun.ultimatespellsystem.bukkit.entities.CustomEntity;
import fr.jamailun.ultimatespellsystem.bukkit.entities.Orb;
import fr.jamailun.ultimatespellsystem.bukkit.entities.UssEntityType;
import fr.jamailun.ultimatespellsystem.dsl.registries.EntityTypeRegistry;
import org.bukkit.entity.EntityType;

import java.util.HashMap;
import java.util.Map;

public final class EntityTypeProvider {
    private EntityTypeProvider() {}

    private final static Map<String, UssEntityType> REGISTERED_TYPES = new HashMap<>();

    static {
        // Bukkit
        for(EntityType type : EntityTypeRegistry.DEFAULT_TYPES) {
            String name = EntityTypeRegistry.prepare(type.name());
            REGISTERED_TYPES.put(name, new UssEntityType(type));
        }
        // Custom
        register("orb", Orb.class);
    }

    public static void register(EntityType bukkitType) {
        String name = EntityTypeRegistry.prepare(bukkitType.name());
        REGISTERED_TYPES.put(name, new UssEntityType(bukkitType));
        EntityTypeRegistry.allow(name);
    }

    public static void register(String key, Class<? extends CustomEntity> customType) {
        String name = EntityTypeRegistry.prepare(key);
        REGISTERED_TYPES.put(name, new UssEntityType(customType));
        EntityTypeRegistry.allow(name);
    }

    public static UssEntityType find(String name) {
        UssEntityType type = REGISTERED_TYPES.get(EntityTypeRegistry.prepare(name));
        if(type == null)
            throw new RuntimeException("Undefined entity type: '" + name + "'. You probably registered a type to the EntityTypeRegistry, but not in the EntityTypeProvider");
        return type;
    }

    public static void loadDefaults() {/* load class and run static blocks ! */}
}
