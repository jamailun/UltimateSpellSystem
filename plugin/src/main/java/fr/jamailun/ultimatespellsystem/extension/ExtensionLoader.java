package fr.jamailun.ultimatespellsystem.extension;

import fr.jamailun.ultimatespellsystem.UssLogger;
import fr.jamailun.ultimatespellsystem.api.providers.AlliesProvider;
import fr.jamailun.ultimatespellsystem.api.providers.CallbackEventProvider;
import fr.jamailun.ultimatespellsystem.api.providers.JavaFunctionProvider;
import fr.jamailun.ultimatespellsystem.extension.allies.VanillaTeamAllies;
import fr.jamailun.ultimatespellsystem.extension.callbacks.CallbackProvider;
import fr.jamailun.ultimatespellsystem.extension.callbacks.EntityDeathCallbacks;
import fr.jamailun.ultimatespellsystem.extension.callbacks.SummonExpiresCallbacks;
import fr.jamailun.ultimatespellsystem.extension.callbacks.ProjectileLandCallbacks;
import fr.jamailun.ultimatespellsystem.extension.functions.*;
import fr.jamailun.ultimatespellsystem.extension.listeners.EntityMoveListener;
import fr.jamailun.ultimatespellsystem.extension.providers.*;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

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
            UssLogger.logWarning("Extension already loaded.");
            return;
        }
        loaded = true;
        UssLogger.logInfo("Loading extension.");

        // Functions
        JavaFunctionProvider.instance().registerFunction(new RayCastFunction(), "raycast_block");
        JavaFunctionProvider.instance().registerFunction(new CastSpellFunction());
        JavaFunctionProvider.instance().registerFunction(new DistanceFunction(), "dist");
        JavaFunctionProvider.instance().registerFunction(new IsValidFunction());
        JavaFunctionProvider.instance().registerFunction(new SetFireFunction());
        JavaFunctionProvider.instance().registerFunction(new StrikeFunction());
        JavaFunctionProvider.instance().registerFunction(new DamageFunction());
        JavaFunctionProvider.instance().registerFunction(new RandFunction(), "rand_num");
        JavaFunctionProvider.instance().registerFunction(new KnockbackFunction(), "set_velocity");
        JavaFunctionProvider.instance().registerFunction(new DirectionOfFunction(), "dir_of");
        JavaFunctionProvider.instance().registerFunction(new NormalizeFunction(), "norm");
        JavaFunctionProvider.instance().registerFunction(new GetHealthFunction());
        JavaFunctionProvider.instance().registerFunction(new GetMaxHealthFunction());
        JavaFunctionProvider.instance().registerFunction(new HealEntityFunction(), "heal_entity");
        JavaFunctionProvider.instance().registerFunction(new LocationToListFunction());

        // Others
        EntityAttributes.register();
        Scopes.register();
        ParticleShapes.register();
        ItemProperties.register();
        EntityTypes.register();
        AlliesProvider.instance().register(new VanillaTeamAllies(), "vanilla-teams");

        UssLogger.logInfo("Loaded extension.");
    }

    public static void loadCallbacks(JavaPlugin plugin) {
        if(loadedCallbacks) {
            UssLogger.logWarning("Extension callbacks already loaded.");
            return;
        }
        loadedCallbacks = true;
        UssLogger.logInfo("Loading extension callbacks and listeners.");

        // Load elements
        loadCallback(plugin, new ProjectileLandCallbacks());
        loadCallback(plugin, new EntityDeathCallbacks());
        loadCallback(plugin, new SummonExpiresCallbacks());

        // Load listeners
        registerEvents(plugin, new EntityMoveListener());

        UssLogger.logInfo("Loaded extension callbacks and listeners.");
    }

    private static void loadCallback(JavaPlugin plugin, CallbackProvider<?> callbackProvider) {
        // 1. Event
        registerEvents(plugin, callbackProvider);
        // 2. Register
        callbackProvider.getCallbacks().forEach(CallbackEventProvider.instance()::registerCallback);
    }

    private static void registerEvents(@NotNull Plugin plugin, @NotNull Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, plugin);
    }

}
