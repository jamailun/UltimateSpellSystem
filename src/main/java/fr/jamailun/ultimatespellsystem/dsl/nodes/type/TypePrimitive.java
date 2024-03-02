package fr.jamailun.ultimatespellsystem.dsl.nodes.type;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

public enum TypePrimitive {

    // Primitives
    STRING(String.class),
    NUMBER(Double.class),
    BOOLEAN(Boolean.class),

    // In-game
    DURATION(Duration.class),
    ENTITY(LivingEntity.class),
    ENTITY_TYPE(String.class),
    MATERIAL(Material.class),
    EFFECT_TYPE(PotionEffect.class),
    LOCATION(Location.class),

    // Specials
    PROPERTIES_SET,
    CUSTOM,
    NULL;

    public final Class<?> clazz;

    TypePrimitive() {
        this.clazz = null;
    }
    TypePrimitive(Class<?> clazz) {
        this.clazz = clazz;
    }

    public Type asType() {
        return new Type(this, false);
    }

    public Type asType(boolean collection) {
        return new Type(this, collection);
    }

}
