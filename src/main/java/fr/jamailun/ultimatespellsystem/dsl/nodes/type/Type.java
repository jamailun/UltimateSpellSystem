package fr.jamailun.ultimatespellsystem.dsl.nodes.type;

public record Type(TypePrimitive primitive, boolean isCollection) {

    public Type deriveToCollection() {
        return new Type(primitive, true);
    }

    public Type deriveToMono() {
        return new Type(primitive, false);
    }

    public boolean is(TypePrimitive primitive) {
        return this.primitive == primitive;
    }

    @Override
    public String toString() {
        return primitive + (isCollection?"[]":"");
    }
}
