package fr.jamailun.ultimatespellsystem.api;

import fr.jamailun.ultimatespellsystem.api.animations.AnimationsManager;
import fr.jamailun.ultimatespellsystem.api.bind.ItemBinder;
import fr.jamailun.ultimatespellsystem.api.bind.SpellCostRegistry;
import fr.jamailun.ultimatespellsystem.api.bind.SpellsTriggerManager;
import fr.jamailun.ultimatespellsystem.api.entities.SummonsManager;
import fr.jamailun.ultimatespellsystem.api.spells.ExternalExecutor;
import fr.jamailun.ultimatespellsystem.api.spells.SpellsManager;
import fr.jamailun.ultimatespellsystem.api.utils.ItemReader;
import fr.jamailun.ultimatespellsystem.api.utils.Scheduler;
import org.bukkit.entity.Entity;
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
     * Get the externa executor. Used to evaluate string expressions.
     * @return non-null instance.
     */
    @NotNull ExternalExecutor getExternalExecutor();

    /**
     * Test if two entity are allied.
     * @param entity first entity.
     * @param other other entity.
     * @return true if they should not attack.
     */
    boolean areAllies(@NotNull Entity entity, @NotNull Entity other);

    /**
     * Reload the configuration.
     */
    void reloadConfiguration();

    /**
     * Get the current version of the plugin.
     * @return a non-null string version.
     */
    @NotNull String getVersion();

    /**
     * Get the default value for tick-rate of custom entities.
     * @return the configuration value.
     */
    int getDefaultCustomEntityTickRate();

}
