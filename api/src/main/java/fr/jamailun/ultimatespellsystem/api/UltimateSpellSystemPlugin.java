package fr.jamailun.ultimatespellsystem.api;

import fr.jamailun.ultimatespellsystem.api.animations.AnimationsManager;
import fr.jamailun.ultimatespellsystem.api.bind.ItemBinder;
import fr.jamailun.ultimatespellsystem.api.bind.SpellCostRegistry;
import fr.jamailun.ultimatespellsystem.api.bind.SpellsTriggerManager;
import fr.jamailun.ultimatespellsystem.api.entities.SummonsManager;
import fr.jamailun.ultimatespellsystem.api.spells.SpellsManager;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

/**
 * Internal interface to implement a USS plugin.
 * @see UltimateSpellSystem
 */
public interface UltimateSpellSystemPlugin {

    /**
     * Log a debug message.
     * @param message message to print.
     */
    void logDebug(@NotNull String message);

    /**
     * Log a message.
     * @param message message to print.
     */
    void logInfo(@NotNull String message);

    /**
     * Log a warning message.
     * @param message message to print.
     */
    void logWarning(@NotNull String message);

    /**
     * Log an error message.
     * @param message message to print.
     */
    void logError(@NotNull String message);

    /**
     * Get the spell manager.
     * @return non-null instance.
     */
    @NotNull SpellsManager getSpellsManager();

    /**
     * Get the summons manager.
     * @return non-null instance.
     */
    @NotNull SummonsManager getSummonsManager();

    /**
     * Get the item-binder.
     * @return non-null instance.
     */
    @NotNull ItemBinder getItemBinder();

    /**
     * Get the spell manager.
     * @return non-null instance.
     */
    @NotNull AnimationsManager getAnimationsManager();

    /**
     * Get the spell-cost registry.
     * @return non-null instance.
     */
    @NotNull SpellCostRegistry getSpellCostRegistry();

    /**
     * Get the spell-trigger manager.
     * @return non-null instance.
     */
    @NotNull SpellsTriggerManager getSpellsTriggerManager();

    @NotNull BukkitRunnable runTaskLater(@NotNull Runnable runnable, long ticks);

    @NotNull BukkitRunnable runTaskRepeat(@NotNull Runnable runnable, int amount, long delay, long period);

    @NotNull BukkitRunnable runTaskRepeat(Runnable runnable, long delay, long period);

    /**
     * Reload the configuration.
     */
    void reloadConfiguration();

}
