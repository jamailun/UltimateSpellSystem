package fr.jamailun.ultimatespellsystem.bukkit;

import fr.jamailun.ultimatespellsystem.bukkit.bind.ItemBinder;
import fr.jamailun.ultimatespellsystem.bukkit.commands.UssCommand;
import fr.jamailun.ultimatespellsystem.bukkit.entities.SummonsManager;
import fr.jamailun.ultimatespellsystem.bukkit.listeners.AggroListener;
import fr.jamailun.ultimatespellsystem.bukkit.listeners.AttackListener;
import fr.jamailun.ultimatespellsystem.bukkit.providers.EntityTypeProvider;
import fr.jamailun.ultimatespellsystem.bukkit.listeners.ItemBoundInteractListener;
import fr.jamailun.ultimatespellsystem.bukkit.spells.SpellsManager;
import fr.jamailun.ultimatespellsystem.bukkit.utils.UssConfig;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.List;

public final class UltimateSpellSystem extends JavaPlugin {

    private static UltimateSpellSystem instance;
    public static UltimateSpellSystem instance() {
        return instance;
    }

    private SpellsManager spellsManager;
    private SummonsManager summonsManager;
    private ItemBinder itemBinder;

    private final UssConfig config = new UssConfig();

    public static final String PREFIX = "§b§lUSS§d | §f";

    @Override
    public void onLoad() {
        EntityTypeProvider.loadDefaults();
    }

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        // Config
        reloadConfigContent();
        saveConfig();

        // Managers
        itemBinder = new ItemBinder();
        spellsManager = new SpellsManager(new File(getDataFolder(), "spells"));
        summonsManager = new SummonsManager();

        // Commands
        new UssCommand();

        // Listeners
        Bukkit.getPluginManager().registerEvents(new ItemBoundInteractListener(itemBinder, config), this);
        Bukkit.getPluginManager().registerEvents(new AttackListener(itemBinder, config), this);
        Bukkit.getPluginManager().registerEvents(new AggroListener(), this);

        logInfo("Plugin loaded.");
    }

    public static void reloadConfigContent() {
        instance().config.reload(instance.getConfig());

        logDebug("Debug mode enabled.");
    }

    @Override
    public void onDisable() {
        summonsManager.purgeAll();
        logInfo("Plugin disabled.");
    }

    public static void logDebug(String message) {
        if(instance().config.isDebug())
            Bukkit.getConsoleSender().sendMessage(PREFIX + "§9DEBUG | §7" + message);
    }
    public static void logInfo(String message) {
        Bukkit.getConsoleSender().sendMessage(PREFIX + "§3INFO  | §f" + message);
    }
    public static void logWarning(String message) {
        Bukkit.getConsoleSender().sendMessage(PREFIX + "§6WARN  | §e" + message);
    }
    public static void logError(String message) {
        Bukkit.getConsoleSender().sendMessage(PREFIX + "§4ERROR | §c" + message);
    }

    public static @NotNull SpellsManager getSpellsManager() {
        return instance.spellsManager;
    }
    public static @NotNull SummonsManager getSummonsManager() {
        return instance.summonsManager;
    }

    public static @NotNull BukkitRunnable runTaskLater(@NotNull Runnable runnable, long ticks) {
        BukkitRunnable task = new BukkitRunnable() {public void run() {runnable.run();}};
        task.runTaskLater(instance, ticks);
        return task;
    }
    public static void runTaskRepeat(@NotNull Runnable runnable, int amount, long delay, long period) {
        new BukkitRunnable() {
            private int count = 0;
            @Override
            public void run() {
                runnable.run();
                count++;
                if(count == amount)
                    cancel();
            }
        }.runTaskTimer(instance, delay, period);
    }

    public static @NotNull BukkitRunnable runTaskRepeat(Runnable runnable, long delay, long period) {
        BukkitRunnable task = new BukkitRunnable() {public void run() {runnable.run();}};
        task.runTaskTimer(instance, delay, period);
        return task;
    }

    public static @NotNull ItemBinder getItemBinder() {
        return instance.itemBinder;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return super.onTabComplete(sender, command, alias, args);
    }
}
