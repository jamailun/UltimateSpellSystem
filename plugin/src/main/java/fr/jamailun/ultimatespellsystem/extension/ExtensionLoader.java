package fr.jamailun.ultimatespellsystem.extension;

import fr.jamailun.ultimatespellsystem.api.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.api.providers.JavaFunctionProvider;
import fr.jamailun.ultimatespellsystem.extension.functions.*;
import fr.jamailun.ultimatespellsystem.extension.providers.EntityTypes;
import fr.jamailun.ultimatespellsystem.extension.providers.ItemProperties;
import fr.jamailun.ultimatespellsystem.extension.providers.ParticleShapes;
import fr.jamailun.ultimatespellsystem.extension.providers.Scopes;

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
        JavaFunctionProvider.instance().register(new IsValidFunction(), "is_valid");
        JavaFunctionProvider.instance().register(new SetFireFunction(), "set_fire");

        // Others
        Scopes.register();
        ParticleShapes.register();
        ItemProperties.register();
        EntityTypes.register();

        UltimateSpellSystem.logInfo("Loaded extension.");
    }

}
