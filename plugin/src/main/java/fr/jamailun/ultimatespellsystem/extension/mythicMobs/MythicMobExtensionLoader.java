package fr.jamailun.ultimatespellsystem.extension.mythicMobs;

import fr.jamailun.ultimatespellsystem.UssLogger;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class MythicMobExtensionLoader {
    private static boolean initialized = false;

    public static void initialize(@NotNull JavaPlugin plugin) {
        if(initialized) return;
        initialized = true;

        Plugin mythicMobs = Bukkit.getServer().getPluginManager().getPlugin("MythicMobs");
        if (mythicMobs == null || !mythicMobs.isEnabled()) {
            return;
        }

        UssLogger.logInfo("MythicMobs found. Registering extension.");

        // Auto-configuration listener
        Bukkit.getPluginManager().registerEvents(new MythicMobConfigurer(), plugin);
    }

}
