package fr.jamailun.ultimatespellsystem.bukkit;

import fr.jamailun.ultimatespellsystem.api.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.api.UltimateSpellSystemPlugin;
import fr.jamailun.ultimatespellsystem.bukkit.animations.AnimationsManagerImpl;
import fr.jamailun.ultimatespellsystem.bukkit.bind.ItemBinderImpl;
import fr.jamailun.ultimatespellsystem.bukkit.commands.UssCommand;
import fr.jamailun.ultimatespellsystem.bukkit.entities.SummonsManagerImpl;
import fr.jamailun.ultimatespellsystem.bukkit.listeners.AggroListener;
import fr.jamailun.ultimatespellsystem.bukkit.listeners.AttackListener;
import fr.jamailun.ultimatespellsystem.bukkit.listeners.EntityDeathListener;
import fr.jamailun.ultimatespellsystem.bukkit.providers.EntityTypeProvider;
import fr.jamailun.ultimatespellsystem.bukkit.listeners.ItemBoundInteractListener;
import fr.jamailun.ultimatespellsystem.bukkit.spells.SpellsManagerImpl;
import fr.jamailun.ultimatespellsystem.bukkit.utils.UssConfig;
import fr.jamailun.ultimatespellsystem.extension.ExtensionLoader;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.List;

/**
 * The {@link JavaPlugin} of USS, and {@link UltimateSpellSystemPlugin} implementation.
 */
public final class UssMain extends JavaPlugin implements UltimateSpellSystemPlugin {

    @Getter private SpellsManagerImpl spellsManager;
    @Getter private SummonsManagerImpl summonsManager;
    @Getter private ItemBinderImpl itemBinder;
    @Getter private AnimationsManagerImpl animationsManager;

    private final UssConfig config = new UssConfig();

    public static final String PREFIX = "§b§lUSS§d | §f";

    @Override
    public void onLoad() {
        UltimateSpellSystem.setPlugin(this);

        ExtensionLoader.load();
        EntityTypeProvider.loadDefaults();
        UssKeys.initialize(this);
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();

        // Config
        reloadConfiguration();
        saveConfig();

        // Managers
        itemBinder = new ItemBinderImpl();
        spellsManager = new SpellsManagerImpl(new File(getDataFolder(), "spells"));
        summonsManager = new SummonsManagerImpl(config);

        // Commands
        new UssCommand();

        // Listeners
        Bukkit.getPluginManager().registerEvents(new ItemBoundInteractListener(itemBinder, config), this);
        Bukkit.getPluginManager().registerEvents(new AttackListener(itemBinder, config), this);
        Bukkit.getPluginManager().registerEvents(new AggroListener(), this);
        Bukkit.getPluginManager().registerEvents(new EntityDeathListener(), this);

        logInfo("Plugin loaded.");
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
        logInfo("Plugin disabled.");
    }

    @Override
    public void logDebug(@NotNull String message) {
        if(config.isDebug())
            Bukkit.getConsoleSender().sendMessage(PREFIX + "§9DEBUG | §7" + message);
    }

    @Override
    public void logInfo(@NotNull String message) {
        Bukkit.getConsoleSender().sendMessage(PREFIX + "§3INFO  | §f" + message);
    }

    @Override
    public void logWarning(@NotNull String message) {
        Bukkit.getConsoleSender().sendMessage(PREFIX + "§6WARN  | §e" + message);
    }

    @Override
    public void logError(@NotNull String message) {
        Bukkit.getConsoleSender().sendMessage(PREFIX + "§4ERROR | §c" + message);
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
}
