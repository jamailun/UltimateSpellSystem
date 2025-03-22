package fr.jamailun.ultimatespellsystem.extension;

import fr.jamailun.ultimatespellsystem.api.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.api.providers.CallbackEventProvider;
import fr.jamailun.ultimatespellsystem.api.providers.JavaFunctionProvider;
import fr.jamailun.ultimatespellsystem.extension.callbacks.CallbackProvider;
import fr.jamailun.ultimatespellsystem.extension.callbacks.EntityDeathCallbacks;
import fr.jamailun.ultimatespellsystem.extension.callbacks.SummonExpiresCallbacks;
import fr.jamailun.ultimatespellsystem.extension.callbacks.ProjectileLandCallbacks;
import fr.jamailun.ultimatespellsystem.extension.functions.*;
import fr.jamailun.ultimatespellsystem.extension.providers.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Extension loader.
 */
public final class ExtensionLoader {
    private ExtensionLoader() {}
    private static boolean loaded = false;
    private static boolean loadedCallbacks = false;

    /**
     * Load the internal extension.
     */
    public static synchronized void loadStatic() {
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
        EntityAttributes.register();
        Scopes.register();
        ParticleShapes.register();
        ItemProperties.register();
        EntityTypes.register();

        UltimateSpellSystem.logInfo("Loaded extension.");
    }

    public static void loadCallbacks(JavaPlugin plugin) {
        if(loadedCallbacks) {
            UltimateSpellSystem.logWarning("Extension callbacks already loaded.");
            return;
        }
        loadedCallbacks = true;
        UltimateSpellSystem.logInfo("Loading extension callbacks.");

        // Load elements
        loadCallback(plugin, new ProjectileLandCallbacks());
        loadCallback(plugin, new EntityDeathCallbacks());
        loadCallback(plugin, new SummonExpiresCallbacks());

        UltimateSpellSystem.logInfo("Loaded extension callbacks.");
    }

    private static void loadCallback(JavaPlugin plugin, CallbackProvider<?> callbackProvider) {
        // 1. Event
        Bukkit.getPluginManager().registerEvents(callbackProvider, plugin);
        // 2. Register
        callbackProvider.getCallbacks().forEach(CallbackEventProvider.instance()::registerCallback);
    }

}
