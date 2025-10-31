package fr.jamailun.ultimatespellsystem.plugin.runner.structs;

import fr.jamailun.ultimatespellsystem.dsl2.tokenization.TokenPosition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Structure instance for an object.
 */
public abstract class AbstractStructInstance<S> implements Struct {

    protected final AbstractStructDefinition<S> definition;
    protected final S object;

    /**
     * Wraps an object inside this definition.
     * @param definition definition definition to use.
     * @param object object to handle.
     */
    public AbstractStructInstance(@NotNull AbstractStructDefinition<S> definition, @NotNull S object) {
        this.definition = definition;
        this.object = object;
    }

    @Override
    public @Nullable Object getField(@NotNull TokenPosition pos, @NotNull String fieldName) {
        return definition.get(object, pos, fieldName);
    }

    @Override
    public void setField(@NotNull TokenPosition pos, @NotNull String fieldName, @Nullable Object value) {
        definition.set(object, pos, fieldName, value);
    }
}
