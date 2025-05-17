package fr.jamailun.ultimatespellsystem;

import fr.jamailun.ultimatespellsystem.api.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.api.UltimateSpellSystemPlugin;
import fr.jamailun.ultimatespellsystem.api.bind.SpellCostRegistry;
import fr.jamailun.ultimatespellsystem.api.bind.SpellsTriggerManager;
import fr.jamailun.ultimatespellsystem.api.spells.ExternalExecutor;
import fr.jamailun.ultimatespellsystem.api.utils.ItemReader;
import fr.jamailun.ultimatespellsystem.api.utils.Scheduler;
import fr.jamailun.ultimatespellsystem.plugin.animations.AnimationsManagerImpl;
import fr.jamailun.ultimatespellsystem.plugin.bind.ItemBinderImpl;
import fr.jamailun.ultimatespellsystem.plugin.bind.costs.SpellCostFactory;
import fr.jamailun.ultimatespellsystem.plugin.bind.trigger.SpellTriggerManagerImpl;
import fr.jamailun.ultimatespellsystem.plugin.commands.UssCommand;
import fr.jamailun.ultimatespellsystem.plugin.entities.SummonsManagerImpl;
import fr.jamailun.ultimatespellsystem.plugin.listeners.*;
import fr.jamailun.ultimatespellsystem.plugin.runner.nodes.functions.SendAttributeNode;
import fr.jamailun.ultimatespellsystem.plugin.spells.SpellsManagerImpl;
import fr.jamailun.ultimatespellsystem.plugin.spells.external.ExternalExecutorImpl;
import fr.jamailun.ultimatespellsystem.plugin.updater.UpdateCheck;
import fr.jamailun.ultimatespellsystem.plugin.configuration.UssConfig;
import fr.jamailun.ultimatespellsystem.extension.ExtensionLoader;
import fr.jamailun.ultimatespellsystem.plugin.utils.ItemReaderImpl;
import fr.jamailun.ultimatespellsystem.plugin.utils.bstats.Metrics;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

/**
 * The {@link JavaPlugin} of USS, and {@link UltimateSpellSystemPlugin} implementation.
 */
public final class UssMain extends JavaPlugin implements UltimateSpellSystemPlugin {

    @Getter private SpellsManagerImpl spellsManager;
    @Getter private SummonsManagerImpl summonsManager;
    @Getter private ItemBinderImpl itemBinder;
    @Getter private AnimationsManagerImpl animationsManager;
    @Getter private final Scheduler scheduler = new UssScheduler(this);
    @Getter private final ItemReader itemReader = new ItemReaderImpl();
    @Getter private final SpellsTriggerManager spellsTriggerManager = new SpellTriggerManagerImpl();
    @Getter private final ExternalExecutor externalExecutor = new ExternalExecutorImpl();

    private UssConfig config;

    public static final String PREFIX = "§b§lUSS§d | §f";

    @Override
    public void onLoad() {
        UltimateSpellSystem.setPlugin(this);

        ExtensionLoader.loadStatic();
        UssKeys.initialize(this);
    }

    @Override
    public void onEnable() {
        config = new UssConfig(this);

        // Callbacks
        ExtensionLoader.loadCallbacks(this);

        // Config
        checkConfigurationVersion();
        reloadConfiguration();

        // Managers
        itemBinder = new ItemBinderImpl();
        spellsManager = new SpellsManagerImpl(getDataFolder());
        summonsManager = new SummonsManagerImpl(config);
        animationsManager = new AnimationsManagerImpl();
        animationsManager.start();

        // Commands
        new UssCommand(this);

        // Listeners
        Bukkit.getPluginManager().registerEvents(new AggroListener(), this);
        Bukkit.getPluginManager().registerEvents(new AttackListener(), this);
        Bukkit.getPluginManager().registerEvents(new EntityDamageListener(), this);
        Bukkit.getPluginManager().registerEvents(new EntityDeathListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerInteractListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerLeaveListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerSneakListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerSwitchItemListener(), this);
        Bukkit.getPluginManager().registerEvents(new BoundSpellCastListener(), this);

        // Others
        new Metrics(this, 24891); // bStats
        ExtensionLoader.loadDependencies();

        UssLogger.logInfo("Plugin loaded.");
        testForLatestVersion();

        // Load spells
        spellsManager.reloadSpells();
    }

    private void checkConfigurationVersion() {
        config.checkVersionAndMigrate();
    }

    @Override
    public void reloadConfiguration() {
        config.reload();
        UssLogger.setDebug(UssConfig.isDebug());
        UssLogger.logDebug("Debug mode enabled."); // will be printed only if debug mode is enabled :)
    }

    @Override
    @SuppressWarnings("UnstableApiUsage")
    public @NotNull String getVersion() {
        return getPluginMeta().getVersion();
    }

    @Override
    public int getDefaultCustomEntityTickRate() {
        return UssConfig.getTicksDefaultCustomEntity();
    }

    @Override
    public void onDisable() {
        summonsManager.purgeAll();
        animationsManager.purge();
        SendAttributeNode.purge();
        UssLogger.logInfo("Plugin disabled.");
    }

    @Override
    public @NotNull SpellCostRegistry getSpellCostRegistry() {
        return SpellCostFactory.instance();
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return super.onTabComplete(sender, command, alias, args);
    }

    /**
     * Async check for an update.
     */
    private void testForLatestVersion() {
        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
            Optional<String> latest = UpdateCheck.getLatestRelease();
            if(latest.isPresent()) {
                UssLogger.logWarning("----------[New update: &2" + latest.get() + "&r]----------");
                UssLogger.logWarning("A new version is available for this USS plugin. Download the latest version to use all the features!");
                UssLogger.logWarning("Go and check &b" + UpdateCheck.getPublicUrl());
                UssLogger.logWarning("---------------------------------------");
            } else {
                String current = getVersion();
                if(current.contains("SNAPSHOT")) {
                    UssLogger.logInfo("You are using an&e experimental&r build (" + current + "). Beware of any issue!");
                } else {
                    UssLogger.logInfo("You have the latest version: &a" + current + "&r.");
                }
            }
        });
    }
}
