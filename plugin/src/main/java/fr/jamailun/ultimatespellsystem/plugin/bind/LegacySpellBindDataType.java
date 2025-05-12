package fr.jamailun.ultimatespellsystem.plugin.bind;

import fr.jamailun.ultimatespellsystem.api.bind.SpellBindData;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

/**
 * Handles serialization of {@link SpellBindData}.
 * Wraps it around a {@link SpellBindDataContainer}.
 */
public class LegacySpellBindDataType implements PersistentDataType<String, SpellBindDataContainer> {

    @Override
    public @NotNull String toPrimitive(@NotNull SpellBindDataContainer cost, @NotNull PersistentDataAdapterContext context) {
        return SpellBindFactory.serializeV1(cost);
    }

    @Override
    public @NotNull SpellBindDataContainer fromPrimitive(@NotNull String primitive, @NotNull PersistentDataAdapterContext context) {
        return SpellBindFactory.deserializeContainerV1(primitive);
    }

    @Override
    public @NotNull Class<String> getPrimitiveType() {
        return String.class;
    }

    @Override
    public @NotNull Class<SpellBindDataContainer> getComplexType() {
        return SpellBindDataContainer.class;
    }
}
