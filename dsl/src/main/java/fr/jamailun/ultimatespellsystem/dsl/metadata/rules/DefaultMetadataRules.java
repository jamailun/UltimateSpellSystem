package fr.jamailun.ultimatespellsystem.dsl.metadata.rules;

import fr.jamailun.ultimatespellsystem.dsl.metadata.MetadataRulesManager;

/**
 * Load the default ruleset for metadata statements.
 */
public final class DefaultMetadataRules {
    private DefaultMetadataRules() {}

    private static boolean initialized = false;

    /**
     * Initialize the metadata rules system.
     */
    public static void initialize() {
        if(initialized) return;
        initialized = true;

        // Register rules
        MetadataRulesManager.registerRule(new ParamDefinitionMetadata());
    }

}
