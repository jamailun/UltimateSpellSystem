package fr.jamailun.ultimatespellsystem.plugin.runner.structs;

import fr.jamailun.ultimatespellsystem.api.entities.SpellEntity;
import org.jetbrains.annotations.NotNull;

/**
 * Structure wrapper for an entity.
 */
public class EntityInstance extends AbstractStructInstance<SpellEntity> {

    /**
     * Wraps a spell-entity inside this definition.
     * @param entity the entity to use.
     */
    public EntityInstance(@NotNull SpellEntity entity, @NotNull EntityDefinition definition) {
        super(definition, entity);
    }

}
