package fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.functions;

import fr.jamailun.ultimatespellsystem.dsl.nodes.type.CollectionFilter;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Type;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import org.jetbrains.annotations.NotNull;

/**
 * A lax-collection type for a function.
 * @param type the primitive of the type.
 * @param collectionFilter filter to determine if the type is, can or cannot be a collection.
 */
public record FunctionType(@NotNull TypePrimitive type, @NotNull CollectionFilter collectionFilter){
    /**
     * Compute a strict {@link Type} correspondance.
     * @return a non-null, new Type instance.
     */
    public @NotNull Type asType() {
        return type.asType(collectionFilter == CollectionFilter.LIST);
    }
}
