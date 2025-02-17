package fr.jamailun.ultimatespellsystem.dsl.nodes.type;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Represents the type of an expression.
 * @param primitive the kind of type.
 * @param isCollection if true, this is a collection of primitives.
 */
public record Type(@NotNull TypePrimitive primitive, boolean isCollection) {

    /**
     * Check if this type is of a primitive.
     * @param primitive the primitive to compare this type with.
     * @return true if the primitives are equals.
     */
    public boolean is(TypePrimitive primitive) {
        return this.primitive == primitive;
    }

    @Contract(pure = true)
    @Override
    public @NotNull String toString() {
        return primitive + (isCollection?"[]":"");
    }

    @Contract(" -> new")
    public @NotNull Type asMonoElement() {
        return new Type(primitive, false);
    }
}
