package fr.jamailun.ultimatespellsystem.dsl.nodes.type;

import org.bukkit.Location;
import org.bukkit.Material;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Enumeration of a possible types.
 */
public enum TypePrimitive {

    // Real Primitives
    STRING(String.class),
    NUMBER(Number.class),
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
        this(null);
    }
    TypePrimitive(Class<?> clazz) {
        this.clazz = clazz;
    }

    /**
     * Convert this enumeration element into a new Type instance.
     * @return a new, non-null Type instance.
     */
    public @NotNull Type asType() {
        return new Type(this, false);
    }

    /**
     * Convert this enumeration element into a new Type instance, possibly a collection.
     * @param collection if true, the returned Type will be a collection.
     * @return a new, non-null Type instance.
     */
    @Contract("_ -> new")
    public @NotNull Type asType(boolean collection) {
        return new Type(this, collection);
    }

}
