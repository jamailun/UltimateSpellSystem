package fr.jamailun.ultimatespellsystem.plugin.bind.costs;

import fr.jamailun.ultimatespellsystem.UssLogger;
import fr.jamailun.ultimatespellsystem.api.bind.SpellCost;
import fr.jamailun.ultimatespellsystem.api.bind.SpellCostArgType;
import fr.jamailun.ultimatespellsystem.api.bind.SpellCostEntry;
import fr.jamailun.ultimatespellsystem.api.bind.SpellCostRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

/**
 * Factory for spell costs.
 */
public final class SpellCostFactory implements SpellCostRegistry {

    private static final SpellCostFactory INSTANCE = new SpellCostFactory();
    private final Map<String, SpellCostEntry<?>> entries = new HashMap<>();
    private final Map<Class<?>, SpellCostEntry<?>> entriesPerClass = new HashMap<>();

    private SpellCostFactory() {
        register(SpellCostEntry.of("durability", DurabilitySpellCost.class, DurabilitySpellCost::new, SpellCostArgType.INTEGER));
        register(SpellCostEntry.of("experience", ExperienceSpellCost.class, ExperienceSpellCost::new, SpellCostArgType.INTEGER, SpellCostArgType.BOOLEAN));
        register(SpellCostEntry.of("food-level", FoodLevelSpellCost.class, FoodLevelSpellCost::new, SpellCostArgType.INTEGER));
        register(SpellCostEntry.of("health", HealthSpellCost.class, HealthSpellCost::new, SpellCostArgType.DOUBLE));
        register(SpellCostEntry.of("item-amount", ItemAmountSpellCost.class, ItemAmountSpellCost::new, SpellCostArgType.INTEGER));
        register(SpellCostEntry.of("permission", PermissionCost.class, PermissionCost::new, SpellCostArgType.STRING));
        register(SpellCostEntry.of("none", NoneSpellCost.class, x -> new NoneSpellCost()));
    }

    public static @NotNull JSONObject serialize(@NotNull SpellCost cost) {
        SpellCostEntry<?> entry = INSTANCE.getByClass(cost.getClass());
        if(entry == null) {
            throw new IllegalArgumentException("Cannot serialize cost " + cost + " : register it first !");
        }
        return new JSONObject(Map.of(
                "id", entry.id(),
                "data", new JSONArray(cost.serialize())
        ));
    }

    public static @NotNull SpellCost deserializeV1(@NotNull List<String> parts) {
        String id = parts.getFirst();
        SpellCostEntry<?> entry = INSTANCE.get(id);
        if(entry == null) {
            throw new RuntimeException("No SpellCost class for class '" + id + "'.");
        }
        parts.removeFirst();
        return entry.deserialize(parts);
    }

    public static @NotNull SpellCost deserialize(@NotNull JSONObject json) {
        String id = json.getString("id");
        SpellCostEntry<?> entry = INSTANCE.get(id);
        if(entry == null) {
            throw new RuntimeException("No SpellCost class for class '" + id + "'.");
        }
        return entry.deserialize(json.getJSONArray("data").toList().stream().map(Objects::toString).toList());
    }

    public static @NotNull SpellCostRegistry instance() {
        return INSTANCE;
    }

    @Override
    public void register(@NotNull SpellCostEntry<?> entry) {
        if(entries.containsKey(entry.id())) {
            UssLogger.logWarning("Duplicate spell cost id '" + entry.id() + "'.");
        }
        entries.put(entry.id(), entry);
        entriesPerClass.put(entry.clazz(), entry);
    }

    @Override
    public @NotNull @UnmodifiableView Collection<String> listIds() {
        return Collections.unmodifiableSet(entries.keySet());
    }

    @Override
    public @Nullable SpellCostEntry<?> get(@NotNull String id) {
        return entries.get(id);
    }

    @Override
    public @Nullable SpellCostEntry<?> getByClass(@NotNull Class<? extends SpellCost> clazz) {
        return entriesPerClass.get(clazz);
    }
}
