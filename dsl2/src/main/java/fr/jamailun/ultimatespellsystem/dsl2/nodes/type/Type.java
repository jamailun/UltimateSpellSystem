package fr.jamailun.ultimatespellsystem.dsl2.nodes.type;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;

/**
 * Represents the type of expression.
 */
public class Type {

    private final @Nullable TypePrimitive primitive;
    private final @Nullable String objectClass;
    private final boolean isNull;
    private final int arrayLevel;

    /**
     * The null value.
     */
    public static final Type NULL = new Type(null, null, 0);

    public Type(@NotNull TypePrimitive primitive, int arrayLevel) {
        this.primitive = primitive;
        this.objectClass = null;
        this.arrayLevel = arrayLevel;
        this.isNull = false;
    }

    public Type(@NotNull String objectClass, int arrayLevel) {
        this.primitive = null;
        this.objectClass = objectClass;
        this.arrayLevel = arrayLevel;
        this.isNull = false;
    }

    private Type(@Nullable TypePrimitive primitive, @Nullable String objectClass, int arrayLevel) {
        this.primitive = primitive;
        this.objectClass = objectClass;
        this.arrayLevel = arrayLevel;
        this.isNull = (primitive == null && objectClass == null);
        if(isNull && arrayLevel > 0)
            throw new IllegalArgumentException("Cannot create a NULL type with an array value.");
    }

    /**
     * Check if this type is of a primitive.
     * @param primitive the primitive to compare this type with.
     * @return true if the primitives are equals.
     */
    public boolean is(@NotNull TypePrimitive primitive) {
        return this.primitive != null && this.primitive == primitive;
    }

    public boolean isOneOf(@NotNull TypePrimitive... primitives) {
        if(this.primitive == null) return false;
        return Set.of(primitives).contains(primitive);
    }

    public boolean isOneOf(@NotNull Collection<Type> types) {
        return types.stream().anyMatch(this::equals);
    }

    public boolean is(@NotNull String objectClass) {
        return this.objectClass != null && this.objectClass.equals(objectClass);
    }

    public boolean is(@NotNull Type type) {
        return this.equals(type);
    }

    public boolean isCollection() {
        return arrayLevel > 0;
    }

    public boolean isNull() {
        return isNull;
    }

    public boolean isPrimitive() {
        return primitive != null;
    }

    @Contract(pure = true)
    public @NotNull Type popArray() {
        return new Type(primitive, objectClass, Math.max(0, arrayLevel - 1));
    }

    @Contract(pure = true)
    public @NotNull Type pushArray() {
        return new Type(primitive, objectClass, arrayLevel + 1);
    }

    public @NotNull String getName() {
        return primitive == null ? Objects.requireNonNull(objectClass) : primitive.name().toLowerCase();
    }

    @Contract(pure = true)
    @Override
    public @NotNull String toString() {
        String arraySuffix = "[]".repeat(arrayLevel);
        return (primitive==null?objectClass: primitive.name()) + arraySuffix;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Type type = (Type) o;
        return arrayLevel == type.arrayLevel && primitive == type.primitive && Objects.equals(objectClass, type.objectClass);
    }

    @Override
    public int hashCode() {
        return Objects.hash(primitive, objectClass, arrayLevel);
    }

    public static @NotNull Type of(@NotNull String name) {
        return new Type(name, 0);
    }
    public static @NotNull Type of(@NotNull TypePrimitive name) {
        return new Type(name, 0);
    }

    /**
     * Get the {@link Type} of an identifier.
     * @param name the identifier to use.
     * @return a non-null Type, either with a primitive or not.
     */
    public static @NotNull Type ofAny(@NotNull String name) {
        TypePrimitive primitive = TypePrimitive.parsePrimitive(name);
        return primitive == null ? Type.of(name) : Type.of(primitive);
    }
}
