package fr.jamailun.ultimatespellsystem.dsl.nodes.type;

public enum TypePrimitive {

    // Primitives
    STRING,
    NUMBER,
    BOOLEAN,

    // In-game
    DISTANCE,
    DURATION,
    ENTITY,
    ENTITY_TYPE,
    MATERIAL_TYPE,
    EFFECT_TYPE,

    // Null
    NULL;


    public Type asType() {
        return new Type(this, false);
    }

    public Type asType(boolean collection) {
        return new Type(this, collection);
    }

}
