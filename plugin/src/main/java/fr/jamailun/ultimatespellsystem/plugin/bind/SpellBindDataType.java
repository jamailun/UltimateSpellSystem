package fr.jamailun.ultimatespellsystem.plugin.bind;

import fr.jamailun.ultimatespellsystem.api.bind.SpellBindData;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

/**
 * Handles serialization of {@link SpellBindData} (in the form of {@link SpellBindDataContainer}).
 * Wraps it around a {@link SpellBindDataContainer}.
 */
public class SpellBindDataType implements PersistentDataType<String, SpellBindDataContainer> {

    @Override
    public @NotNull String toPrimitive(@NotNull SpellBindDataContainer container, @NotNull PersistentDataAdapterContext context) {
        return SpellBindDataSerializer.encodeSpellBindData(container);
    }

    @Override
    public @NotNull SpellBindDataContainer fromPrimitive(@NotNull String encoded, @NotNull PersistentDataAdapterContext context) {
        return SpellBindDataSerializer.decodeSpellBindData(encoded);
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
