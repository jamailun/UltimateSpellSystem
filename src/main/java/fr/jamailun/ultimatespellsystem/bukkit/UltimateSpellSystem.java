package fr.jamailun.ultimatespellsystem.bukkit;

import fr.jamailun.ultimatespellsystem.bukkit.commands.UssCommand;
import fr.jamailun.ultimatespellsystem.bukkit.entities.OrbArea;
import fr.jamailun.ultimatespellsystem.bukkit.extensible.EntityTypeProvider;
import fr.jamailun.ultimatespellsystem.bukkit.spells.SpellsManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.List;

public final class UltimateSpellSystem extends JavaPlugin {

    private static UltimateSpellSystem instance;

    private SpellsManager manager;

    private boolean configDebug;

    public static final String PREFIX = "§b§lUSS§d | §f";

    // Register provider
    static {
        EntityTypeProvider.register("orb", OrbArea.class);
    }

    @Override
    public void onEnable() {
        instance = this;

        // Config
        reloadConfigContent();
        saveConfig();

        // Enable
        new UssCommand();

        manager = new SpellsManager(new File(getDataFolder(), "spells"));
    }

    public static void reloadConfigContent() {
        FileConfiguration config = instance.getConfig();
        instance.configDebug = config.getBoolean("debug", false);

        logDebug("Debug mode enabled.");
    }

    @Override
    public void onDisable() {

    }

    public static void logDebug(String message) {
        if(instance.configDebug)
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

    public static SpellsManager getSpellsManager() {
        return instance.manager;
    }

    public static BukkitRunnable runTaskLater(Runnable runnable, long ticks) {
        BukkitRunnable task = new BukkitRunnable() {public void run() {runnable.run();}};
        task.runTaskLater(instance, ticks);
        return task;
    }
    public static void runTaskRepeat(Runnable runnable, int amount, long delay, long period) {
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

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return super.onTabComplete(sender, command, alias, args);
    }
}
