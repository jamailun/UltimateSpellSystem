package fr.jamailun.ultimatespellsystem.dsl.nodes.type;

/**
 * Represents the type of an expression.
 * @param primitive the kind of type.
 * @param isCollection if true, this is a collection of primitives.
 */
public record Type(TypePrimitive primitive, boolean isCollection) {

    /**
     * Check if this type is of a primitive.
     * @param primitive the primitive to compare this type with.
     * @return true if the primitives are equals.
     */
    public boolean is(TypePrimitive primitive) {
        return this.primitive == primitive;
    }

    @Override
    public String toString() {
        return primitive + (isCollection?"[]":"");
    }
}
