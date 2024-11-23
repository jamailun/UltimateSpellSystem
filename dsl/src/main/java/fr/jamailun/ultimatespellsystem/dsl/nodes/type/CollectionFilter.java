package fr.jamailun.ultimatespellsystem.dsl.nodes.type;

import org.jetbrains.annotations.NotNull;

/**
 * Allow to check if a Type is a collection or not.
 */
public enum CollectionFilter {

    /**
     * Only accepts mono-elements.
     */
    MONO_ELEMENT,

    /**
     * Only accept collections.
     */
    LIST,

    /**
     * Accepts both.
     */
    ANY;

    /**
     * Test if a type matches the filter.
     * @param type the type to test.
     * @return true if the type matches the filter.
     */
    public boolean isValid(@NotNull Type type) {
        return switch (this) {
            case MONO_ELEMENT -> ! type.isCollection();
            case LIST -> type.isCollection();
            case ANY -> true;
        };
    }

}
