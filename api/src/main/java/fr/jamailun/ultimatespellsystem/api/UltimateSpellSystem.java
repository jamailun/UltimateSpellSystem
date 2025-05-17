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
import org.jetbrains.annotations.NotNull;

/**
 * Entry point of the <b>Ultimate Spells System</b> API.
 */
public final class UltimateSpellSystem {
    private UltimateSpellSystem() {/* No instanciation */}

    private static UltimateSpellSystemPlugin plugin;

    /**
     * Internal setter for the plugin instance. Do not call this : it will break everything.
     * @param plugin plugin instance.
     */
    public static void setPlugin(@NotNull UltimateSpellSystemPlugin plugin) {
        if(UltimateSpellSystem.plugin != null)
            throw new IllegalStateException("Cannot set the USS plugin twice.");
        UltimateSpellSystem.plugin = plugin;
    }

    /**
     * Test if the plugin loaded properly.
     * <br/>
     * Do only call this after checking if the plugin was present: {@code Bukkit.getPluginManager().getPlugin("UltimateSpellSystem") != null}.
     * @return true if the plugin is online and running.
     */
    public static boolean isValid() {
        return plugin != null;
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

    /**
     * Get the spell-cost registry singleton instance.
     * @return the non-null reference.
     */
    public static @NotNull SpellCostRegistry getSpellCostRegistry() {
        return plugin.getSpellCostRegistry();
    }

    /**
     * Get the spells manager for players.
     * @return the non-null reference.
     */
    public static @NotNull SpellsTriggerManager getSpellsTriggerManager() {
        return plugin.getSpellsTriggerManager();
    }

    /**
     * Get the ItemReader singleton, used to deserialize items from spells.
     * @return the non-null reference.
     */
    public static @NotNull ItemReader getItemReader() {
        return plugin.getItemReader();
    }

    /**
     * Get the USS scheduler. If you plan to run tasks in your USS extension, use those. It will allow to plugin
     * to properly control everything it does.
     * @return the non-null reference.
     */
    public static @NotNull Scheduler getScheduler() {
        return plugin.getScheduler();
    }


    /**
     * Get the external executor. Used to evaluate any DSL expressions.
     * @return the non-null reference.
     */
    public static @NotNull ExternalExecutor getExternalExecutor() {
        return plugin.getExternalExecutor();
    }

    /**
     * Reload the configuration.
     */
    public static void reloadConfiguration() {
        plugin.reloadConfiguration();
    }

    /**
     * Get the current version of the plugin.<br/>
     * The output format is: {@code <MAJOR>.<MINOR>.<PATCH>[-SNAPSHOT]}.
     * @return a non-null string version.
     */
    public static @NotNull String getVersion() {
        return plugin.getVersion();
    }

    /**
     * Get the default value for tick-rate of custom entities.
     * @return the configuration value.
     */
    public static int getDefaultCustomEntityTickRate() {
        return plugin.getDefaultCustomEntityTickRate();
    }

}
