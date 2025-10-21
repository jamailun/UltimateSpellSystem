package fr.jamailun.ultimatespellsystem.dsl2.nodes.type;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Set;

/**
 * Represents the type of expression.
 */
public class Type {

    private final @Nullable TypePrimitive primitive;
    private final @Nullable String objectClass;
    private final int arrayLevel;

    public Type(@NotNull TypePrimitive primitive, int arrayLevel) {
        this.primitive = primitive;
        this.objectClass = null;
        this.arrayLevel = arrayLevel;
    }

    public Type(@NotNull String objectClass, int arrayLevel) {
        this.primitive = null;
        this.objectClass = objectClass;
        this.arrayLevel = arrayLevel;
    }

    private Type(@Nullable TypePrimitive primitive, @Nullable String objectClass, int arrayLevel) {
        this.primitive = primitive;
        this.objectClass = objectClass;
        this.arrayLevel = arrayLevel;
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

    public boolean is(@NotNull String objectClass) {
        return this.objectClass != null && this.objectClass.equals(objectClass);
    }

    public boolean isCollection() {
        return arrayLevel > 0;
    }

    @Contract(pure = true)
    public @NotNull Type popArray() {
        return new Type(primitive, objectClass, Math.max(0, arrayLevel - 1));
    }

    @Contract(pure = true)
    public @NotNull Type pushArray() {
        return new Type(primitive, objectClass, arrayLevel + 1);
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
}
