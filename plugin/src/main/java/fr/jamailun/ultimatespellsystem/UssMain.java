package fr.jamailun.ultimatespellsystem;

import fr.jamailun.ultimatespellsystem.api.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.api.UltimateSpellSystemPlugin;
import fr.jamailun.ultimatespellsystem.api.bind.SpellCostRegistry;
import fr.jamailun.ultimatespellsystem.api.bind.SpellsTriggerManager;
import fr.jamailun.ultimatespellsystem.plugin.animations.AnimationsManagerImpl;
import fr.jamailun.ultimatespellsystem.plugin.bind.ItemBinderImpl;
import fr.jamailun.ultimatespellsystem.plugin.bind.costs.SpellCostFactory;
import fr.jamailun.ultimatespellsystem.plugin.bind.trigger.SpellTriggerManagerImpl;
import fr.jamailun.ultimatespellsystem.plugin.commands.UssCommand;
import fr.jamailun.ultimatespellsystem.plugin.entities.SummonsManagerImpl;
import fr.jamailun.ultimatespellsystem.plugin.listeners.*;
import fr.jamailun.ultimatespellsystem.plugin.runner.nodes.functions.SendAttributeNode;
import fr.jamailun.ultimatespellsystem.plugin.spells.SpellsManagerImpl;
import fr.jamailun.ultimatespellsystem.plugin.updater.UpdateCheck;
import fr.jamailun.ultimatespellsystem.plugin.utils.UssConfig;
import fr.jamailun.ultimatespellsystem.extension.ExtensionLoader;
import fr.jamailun.ultimatespellsystem.plugin.utils.bstats.Metrics;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
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
    @Getter private final SpellsTriggerManager spellsTriggerManager = new SpellTriggerManagerImpl();

    private final UssConfig config = new UssConfig();

    public static final String PREFIX = "§b§lUSS§d | §f";

    @Override
    public void onLoad() {
        UltimateSpellSystem.setPlugin(this);

        ExtensionLoader.loadStatic();
        UssKeys.initialize(this);
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();

        // Callbacks
        ExtensionLoader.loadCallbacks(this);

        // Config
        reloadConfiguration();
        saveConfig();

        // Managers
        itemBinder = new ItemBinderImpl();
        spellsManager = new SpellsManagerImpl(new File(getDataFolder(), "spells"));
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

        // bStat
        new Metrics(this, 24891);

        logInfo("Plugin loaded.");
        testForLatestVersion();
    }

    @Override
    public void reloadConfiguration() {
        config.reload(getConfig());
        logDebug("Debug mode enabled.");
    }

    @Override
    public void onDisable() {
        summonsManager.purgeAll();
        animationsManager.purge();
        SendAttributeNode.purge();
        logInfo("Plugin disabled.");
    }

    @Override
    public void logDebug(@NotNull String message) {
        if(config.isDebug())
            Bukkit.getConsoleSender().sendMessage(PREFIX + "§9DEBUG | §7" + message);
    }

    @Override
    public void logInfo(@NotNull String message) {
        sendMessage("&3INFO | &f" + message, "&f");
    }

    @Override
    public void logWarning(@NotNull String message) {
        sendMessage("&6WARN | &e" + message, "&e");
    }

    @Override
    public void logError(@NotNull String message) {
        sendMessage("&4ERROR | &c" + message, "&c");
    }

    @Override
    public @NotNull SpellCostRegistry getSpellCostRegistry() {
        return SpellCostFactory.instance();
    }

    @SuppressWarnings("deprecation")
    private void sendMessage(@NotNull String message, @NotNull String color) {
        Bukkit.getConsoleSender().sendMessage(PREFIX + ChatColor.translateAlternateColorCodes('&', message.replace("&r", color)));
    }

    @Override
    public @NotNull BukkitRunnable runTaskLater(@NotNull Runnable runnable, long ticks) {
        BukkitRunnable task = new BukkitRunnable() {public void run() {runnable.run();}};
        task.runTaskLater(this, ticks);
        return task;
    }

    @Override
    public @NotNull BukkitRunnable runTaskRepeat(@NotNull Runnable runnable, int amount, long delay, long period) {
        BukkitRunnable br = new BukkitRunnable() {
            private int count = 0;
            @Override
            public void run() {
                runnable.run();
                count++;
                if(count == amount)
                    cancel();
            }
        };
        br.runTaskTimer(this, delay, period);
        return br;
    }

    @Override
    public @NotNull BukkitRunnable runTaskRepeat(Runnable runnable, long delay, long period) {
        BukkitRunnable task = new BukkitRunnable() {public void run() {runnable.run();}};
        task.runTaskTimer(this, delay, period);
        return task;
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
            Optional<String> latest = UpdateCheck.getLatestRelease(this);
            if(latest.isPresent()) {
                logWarning("----------[New update: &2" + latest.get() + "&r]----------");
                logWarning("A new version is available for this USS plugin. Download the latest version to use all the features!");
                logWarning("Go and check &b" + UpdateCheck.getPublicUrl());
                logWarning("---------------------------------------");
            } else {
                String current = UpdateCheck.getPluginVersion(this);
                if(current.contains("SNAPSHOT")) {
                    logInfo("You are using an&e experimental&r build (" + current + "). Beware of any issue!");
                } else {
                    logInfo("You have the latest version: &a" + current + "&r.");
                }
            }
        });
    }
}
