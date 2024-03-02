package fr.jamailun.ultimatespellsystem.dsl.nodes.type;

import fr.jamailun.ultimatespellsystem.bukkit.spells.SpellEntity;
import org.bukkit.Location;
import org.bukkit.Material;

/**
 * Enumeration of a possible types.
 */
public enum TypePrimitive {

    // Real Primitives
    STRING(String.class),
    NUMBER(Double.class),
    BOOLEAN(Boolean.class),

    // In-game
    DURATION(Duration.class),
    ENTITY(SpellEntity.class),
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
