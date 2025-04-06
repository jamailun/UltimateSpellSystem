package fr.jamailun.ultimatespellsystem.plugin.bind.costs;

import fr.jamailun.ultimatespellsystem.api.bind.SpellCost;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Factory for spell costs.
 */
public final class SpellCostFactory {

    private static final Map<String, Function<List<String>, SpellCost>> DESERIALIZERS = new HashMap<>();

    static {
        register(DurabilitySpellCost.class.getSimpleName(), DurabilitySpellCost::new);
        register(ExperienceSpellCost.class.getSimpleName(), ExperienceSpellCost::new);
        register(FoodLevelSpellCost.class.getSimpleName(), FoodLevelSpellCost::new);
        register(HealthSpellCost.class.getSimpleName(), HealthSpellCost::new);
        register(ItemAmountSpellCost.class.getSimpleName(), ItemAmountSpellCost::new);
    }

    public static @NotNull SpellCost deserialize(@NotNull String raw) {
        List<String> parts = new ArrayList<>(List.of(raw.split(";")));
        String clazz = parts.getFirst();
        if(!DESERIALIZERS.containsKey(clazz)) {
            throw new RuntimeException("No SpellCost class for class '" + clazz + "'.");
        }
        parts.removeFirst();
        return DESERIALIZERS.get(clazz).apply(parts);
    }

    public static void register(@NotNull String clazz, @NotNull Function<List<String>, SpellCost> constructor) {
        DESERIALIZERS.put(clazz, constructor);
    }

}
