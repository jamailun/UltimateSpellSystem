package fr.jamailun.ultimatespellsystem.plugin.bind;

import fr.jamailun.ultimatespellsystem.api.bind.SpellCost;
import fr.jamailun.ultimatespellsystem.plugin.bind.costs.SpellCostFactory;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

/**
 * Handles serialization of {@link SpellCost}.
 */
public class SpellCostDataType implements PersistentDataType<String, SpellCost> {

    @Override
    public @NotNull String toPrimitive(@NotNull SpellCost cost, @NotNull PersistentDataAdapterContext context) {
        return cost.serialize();
    }

    @Override
    public @NotNull SpellCost fromPrimitive(@NotNull String primitive, @NotNull PersistentDataAdapterContext context) {
        return SpellCostFactory.deserialize(primitive);
    }

    @Override
    public @NotNull Class<String> getPrimitiveType() {
        return String.class;
    }

    @Override
    public @NotNull Class<SpellCost> getComplexType() {
        return SpellCost.class;
    }
}
