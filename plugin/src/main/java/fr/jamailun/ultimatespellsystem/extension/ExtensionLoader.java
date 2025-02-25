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
        JavaFunctionProvider.instance().registerFunction(new RayCastFunction(), "raycast_block");
        JavaFunctionProvider.instance().registerFunction(new CastSpellFunction());
        JavaFunctionProvider.instance().registerFunction(new DistanceFunction(), "dist");
        JavaFunctionProvider.instance().registerFunction(new IsValidFunction());
        JavaFunctionProvider.instance().registerFunction(new SetFireFunction());
        JavaFunctionProvider.instance().registerFunction(new StrikeFunction());
        JavaFunctionProvider.instance().registerFunction(new DamageFunction());

        // Others
        Scopes.register();
        ParticleShapes.register();
        ItemProperties.register();
        EntityTypes.register();

        UltimateSpellSystem.logInfo("Loaded extension.");
    }

}
