package fr.jamailun.ultimatespellsystem.api;

import fr.jamailun.ultimatespellsystem.api.bukkit.animations.AnimationsManager;
import fr.jamailun.ultimatespellsystem.api.bukkit.bind.ItemBinder;
import fr.jamailun.ultimatespellsystem.api.bukkit.entities.SummonsManager;
import fr.jamailun.ultimatespellsystem.api.bukkit.spells.SpellsManager;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

/**
 * Entry point of the <b>Ultimate Spells System</b> API.
 */
public final class UltimateSpellSystem {

    private UltimateSpellSystem() {}

    private static UltimateSpellSystemPlugin plugin;
    public static void setPlugin(@NotNull UltimateSpellSystemPlugin plugin) {
        if(UltimateSpellSystem.plugin != null)
            throw new IllegalStateException("Cannot set the USS plugin twice.");
        UltimateSpellSystem.plugin = plugin;
    }

    /**
     * Log a DEBUG message. Will only be sent if debug mode is enabled in the configuration.
     * @param message the message to print.
     */
    public static void logDebug(@NotNull String message) {
        plugin.logDebug(message);
    }

    /**
     * Log a INFO message.
     * @param message the message to print.
     */
    public static void logInfo(@NotNull String message) {
        plugin.logInfo(message);
    }

    /**
     * Log a WARN message.
     * @param message the message to print.
     */
    public static void logWarning(@NotNull String message) {
        plugin.logWarning(message);
    }

    /**
     * Log a ERROR message.
     * @param message the message to print.
     */
    public static void logError(@NotNull String message) {
        plugin.logError(message);
    }

    /**
     * Get the spell manager singleton instance.
     * @return the non-null reference.
     */
    public static @NotNull SpellsManager getSpellsManager() {
        return plugin.getSpellsManager();
    }

    /**
     * Get the summons manager singleton instance.
     * @return the non-null reference.
     */
    public static @NotNull SummonsManager getSummonsManager() {
        return plugin.getSummonsManager();
    }

    /**
     * Get the item binder singleton instance.
     * @return the non-null reference.
     */
    public static @NotNull ItemBinder getItemBinder() {
        return plugin.getItemBinder();
    }

    /**
     * Get the animations manager singleton instance.
     * @return the non-null reference.
     */
    public static @NotNull AnimationsManager getAnimationsManager() {
        return plugin.getAnimationsManager();
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

    /**
     * Reload the configuration.
     */
    public static void reloadConfiguration() {
        plugin.reloadConfiguration();
    }

}
