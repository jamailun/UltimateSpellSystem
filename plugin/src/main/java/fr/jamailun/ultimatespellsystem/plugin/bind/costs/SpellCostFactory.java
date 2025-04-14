package fr.jamailun.ultimatespellsystem.plugin.bind.costs;

import fr.jamailun.ultimatespellsystem.api.bind.SpellCost;
import fr.jamailun.ultimatespellsystem.api.bind.SpellCostArgType;
import fr.jamailun.ultimatespellsystem.api.bind.SpellCostEntry;
import fr.jamailun.ultimatespellsystem.api.bind.SpellCostRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.*;

/**
 * Factory for spell costs.
 */
public final class SpellCostFactory implements SpellCostRegistry {

    private static final SpellCostFactory INSTANCE = new SpellCostFactory();
    private final Map<String, SpellCostEntry> entries = new HashMap<>();

    private SpellCostFactory() {
        register(SpellCostEntry.of("durability", DurabilitySpellCost::new, SpellCostArgType.INTEGER));
        register(SpellCostEntry.of("experience", ExperienceSpellCost::new, SpellCostArgType.INTEGER, SpellCostArgType.BOOLEAN));
        register(SpellCostEntry.of("durability", DurabilitySpellCost::new, SpellCostArgType.INTEGER));
        register(SpellCostEntry.of("food-level", FoodLevelSpellCost::new, SpellCostArgType.INTEGER));
        register(SpellCostEntry.of("health", HealthSpellCost::new, SpellCostArgType.DOUBLE));
        register(SpellCostEntry.of("item-amount", ItemAmountSpellCost::new, SpellCostArgType.INTEGER));
        register(SpellCostEntry.of("none", x -> new NoneSpellCost()));
    }

    public static @NotNull String serialize(@NotNull SpellCost cost) {
        return cost.getClass().getSimpleName() + ";" + cost.serialize();
    }

    public static @NotNull SpellCost deserialize(@NotNull String raw) {
        List<String> parts = new ArrayList<>(List.of(raw.split(";")));
        String id = parts.getFirst();
        SpellCostEntry entry = INSTANCE.get(id);
        if(entry == null) {
            throw new RuntimeException("No SpellCost class for class '" + id + "'.");
        }
        parts.removeFirst();
        return entry.deserialize(parts);
    }

    public static @NotNull SpellCostRegistry instance() {
        return INSTANCE;
    }

    @Override
    public void register(@NotNull SpellCostEntry entry) {
        entries.put(entry.id(), entry);
    }

    @Override
    public @NotNull @UnmodifiableView Collection<String> listIds() {
        return Collections.unmodifiableSet(entries.keySet());
    }

    @Override
    public @Nullable SpellCostEntry get(@NotNull String id) {
        return entries.get(id);
    }
}
