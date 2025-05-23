package fr.jamailun.ultimatespellsystem.dsl.nodes.type;

import org.bukkit.Location;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Enumeration of a possible types.
 */
public enum TypePrimitive {

    // Real Primitives
    /** A String primitive. */
    STRING(String.class),
    /** A number primitive. */
    NUMBER(Number.class),
    /** A boolean primitive. */
    BOOLEAN(Boolean.class),

    // In-game
    /** A temporal duration. */
    DURATION(Duration.class),
    /** An in-game entity. */
    ENTITY(SpellEntity.class),
    /** A Bukkit entity-type. */
    ENTITY_TYPE(String.class),
    /** A Bukkit location. */
    LOCATION(Location.class),

    // Specials
    /** A map of properties. */
    PROPERTIES_SET,
    /** A string for custom values. */
    CUSTOM(String.class),
    /** A {@code null} value. */
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

    /**
     * Parse a string to find the corresponding primitive.
     * @param value the name of the primitive.
     * @return null if no match.
     */
    public static @Nullable TypePrimitive parsePrimitive(@NotNull String value) {
        return switch (value.toLowerCase()) {
            case "string" -> STRING;
            case "number", "double", "float", "integer", "int", "short", "byte" -> NUMBER;
            case "boolean", "bool" -> BOOLEAN;
            case "duration", "time", "chrono" -> DURATION;
            case "entity", "living_entity", "living-entity", "living" -> ENTITY;
            case "entity_type" -> ENTITY_TYPE;
            case "null" -> NULL;
            case "loc", "location", "position", "pos" -> LOCATION;
            case "map", "data", "properties", "properties-set", "properties_set" -> PROPERTIES_SET;
            case "custom", "?", "any" -> CUSTOM;
            default -> null;
        };
    }

}
