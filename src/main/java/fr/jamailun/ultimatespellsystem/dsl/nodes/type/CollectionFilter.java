package fr.jamailun.ultimatespellsystem.dsl.nodes.type;

public enum CollectionFilter {

    MONO_ELEMENT,
    LIST,
    ANY;

    public boolean isValid(Type type) {
        return switch (this) {
            case MONO_ELEMENT -> ! type.isCollection();
            case LIST -> type.isCollection();
            case ANY -> true;
        };
    }

}
