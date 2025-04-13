package fr.jamailun.ultimatespellsystem.plugin.bind;

import fr.jamailun.ultimatespellsystem.api.bind.SpellBindData;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

/**
 * Handles serialization of {@link SpellBindData}.
 */
public class SpellCostDataType implements PersistentDataType<String, SpellBindData> {

    @Override
    public @NotNull String toPrimitive(@NotNull SpellBindData cost, @NotNull PersistentDataAdapterContext context) {
        return SpellBindFactory.serialize(cost);
    }

    @Override
    public @NotNull SpellBindData fromPrimitive(@NotNull String primitive, @NotNull PersistentDataAdapterContext context) {
        return SpellBindFactory.deserialize(primitive);
    }

    @Override
    public @NotNull Class<String> getPrimitiveType() {
        return String.class;
    }

    @Override
    public @NotNull Class<SpellBindData> getComplexType() {
        return SpellBindData.class;
    }
}
