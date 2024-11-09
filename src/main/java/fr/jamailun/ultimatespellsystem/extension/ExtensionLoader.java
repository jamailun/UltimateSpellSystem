package fr.jamailun.ultimatespellsystem.extension;

import fr.jamailun.ultimatespellsystem.bukkit.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.bukkit.providers.JavaFunctionProvider;
import fr.jamailun.ultimatespellsystem.extension.functions.RayCastFunction;

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

        JavaFunctionProvider.instance().register(new RayCastFunction(), "raycast", "raycast_block");

        UltimateSpellSystem.logInfo("Loaded extension.");
    }

}
