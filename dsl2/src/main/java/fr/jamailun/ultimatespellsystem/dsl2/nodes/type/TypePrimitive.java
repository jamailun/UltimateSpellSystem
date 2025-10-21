package fr.jamailun.ultimatespellsystem.dsl2.nodes.type;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

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

    /** A temporal duration. */
    DURATION(Duration.class),

    LOCATION,

    MAP(Map.class),

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
        return new Type(this, 0);
    }

    /**
     * Convert this enumeration element into a new Type instance, possibly a collection.
     * @param collection if true, the returned Type will be a collection.
     * @return a new, non-null Type instance.
     */
    @Contract("_ -> new")
    public @NotNull Type asType(boolean collection) {
        return new Type(this, collection ? 1 : 0);
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
            case "null" -> NULL;
            case "map", "data", "properties", "properties-set", "properties_set" -> MAP;
            default -> null;
        };
    }

}
