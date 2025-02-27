package fr.jamailun.ultimatespellsystem.dsl.metadata;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.*;

/**
 * Register and expose all {@link MetadataRule}.
 */
public final class MetadataRulesManager {
    private MetadataRulesManager() {}

    private static final Map<String, List<MetadataRule>> RULES = new HashMap<>();

    /**
     * Register a new rule.
     * @param rule the rule to register.
     */
    public static void registerRule(@NotNull MetadataRule rule) {
        RULES.putIfAbsent(rule.getName(), new ArrayList<>());
        RULES.get(rule.getName()).add(rule);
    }

    /**
     * Get all rules for a name.
     * @param name a non-null name.
     * @return an unmodifiable list of rules. If not rule exist, the list will be empty.
     */
    public static @NotNull @Unmodifiable List<MetadataRule> getRulesForName(@NotNull String name) {
        return Collections.unmodifiableList(RULES.getOrDefault(name, Collections.emptyList()));
    }

    /**
     * List existing rules.
     * @return an unmodifiable collection of names.
     */
    public static @NotNull @Unmodifiable Collection<String> existingKeys() {
        return Collections.unmodifiableSet( RULES.keySet() );
    }

}
