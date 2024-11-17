package fr.jamailun.ultimatespellsystem.extension;

import fr.jamailun.ultimatespellsystem.api.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.bukkit.providers.JavaFunctionProvider;
import fr.jamailun.ultimatespellsystem.extension.functions.CastSpellFunction;
import fr.jamailun.ultimatespellsystem.extension.functions.DistanceFunction;
import fr.jamailun.ultimatespellsystem.extension.functions.RayCastFunction;
import fr.jamailun.ultimatespellsystem.extension.scopes.EntityTypesScopes;

/**
 * Extension loader.
 */
public final class ExtensionLoader {
    private ExtensionLoader() {}
    private static boolean loaded = false;

    /**
     * Load the internal extension.
     */
    public static synchronized void load() {
        if(loaded) {
            UltimateSpellSystem.logWarning("Extension already loaded.");
            return;
        }
        loaded = true;
        UltimateSpellSystem.logInfo("Loading extension.");

        // Functions
        JavaFunctionProvider.instance().register(new RayCastFunction(), "raycast", "raycast_block");
        JavaFunctionProvider.instance().register(new CastSpellFunction(), "cast");
        JavaFunctionProvider.instance().register(new DistanceFunction(), "distance", "dist");

        // Others
        EntityTypesScopes.register();

        UltimateSpellSystem.logInfo("Loaded extension.");
    }

}
