package fr.jamailun.ultimatespellsystem.api;

import fr.jamailun.ultimatespellsystem.api.animations.AnimationsManager;
import fr.jamailun.ultimatespellsystem.api.bind.ItemBinder;
import fr.jamailun.ultimatespellsystem.api.bind.SpellCostRegistry;
import fr.jamailun.ultimatespellsystem.api.bind.SpellsTriggerManager;
import fr.jamailun.ultimatespellsystem.api.entities.SummonsManager;
import fr.jamailun.ultimatespellsystem.api.spells.SpellsManager;
import fr.jamailun.ultimatespellsystem.api.utils.ItemReader;
import fr.jamailun.ultimatespellsystem.api.utils.Scheduler;
import org.jetbrains.annotations.NotNull;

/**
 * Internal interface to implement a USS plugin.
 * @see UltimateSpellSystem
 */
public interface UltimateSpellSystemPlugin {

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

    /**
     * Get the item-reader.
     * @return non-null instance.
     */
    @NotNull ItemReader getItemReader();

    /**
     * Get the USS scheduler.
     * @return non-null instance.
     */
    @NotNull Scheduler getScheduler();

    /**
     * Reload the configuration.
     */
    void reloadConfiguration();

}
