package fr.jamailun.ultimatespellsystem.api;

import fr.jamailun.ultimatespellsystem.api.bukkit.bind.ItemBinder;
import fr.jamailun.ultimatespellsystem.api.bukkit.entities.SummonsManager;
import fr.jamailun.ultimatespellsystem.api.bukkit.spells.SpellsManager;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public final class UltimateSpellSystem {

    private UltimateSpellSystem() {}

    private static UltimateSpellSystemPlugin plugin;
    public static void setPlugin(@NotNull UltimateSpellSystemPlugin plugin) {
        if(UltimateSpellSystem.plugin != null)
            throw new IllegalStateException("Cannot set the USS plugin twice.");
        UltimateSpellSystem.plugin = plugin;
    }

    public static void logDebug(@NotNull String message) {
        plugin.logDebug(message);
    }

    public static void logInfo(@NotNull String message) {
        plugin.logInfo(message);
    }

    public static void logWarning(@NotNull String message) {
        plugin.logWarning(message);
    }

    public static void logError(@NotNull String message) {
        plugin.logError(message);
    }

    public static @NotNull SpellsManager getSpellsManager() {
        return plugin.getSpellsManager();
    }

    public static @NotNull SummonsManager getSummonsManager() {
        return plugin.getSummonsManager();
    }

    public static @NotNull ItemBinder getItemBinder() {
        return plugin.getItemBinder();
    }

    //TODO move to internal level
    public static @NotNull BukkitRunnable runTaskLater(@NotNull Runnable runnable, long ticks) {
        return plugin.runTaskLater(runnable, ticks);
    }

    //TODO move to internal level
    public static @NotNull BukkitRunnable runTaskRepeat(@NotNull Runnable runnable, int amount, long delay, long period) {
        return plugin.runTaskRepeat(runnable, amount, delay, period);
    }

    //TODO move to internal level
    public static @NotNull BukkitRunnable runTaskRepeat(Runnable runnable, long delay, long period) {
        return plugin.runTaskRepeat(runnable, delay, period);
    }

    public static void reloadConfiguration() {
        plugin.reloadConfiguration();
    }

}
