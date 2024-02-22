package fr.jamailun.ultimatespellsystem;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class UltimateSpellSystem extends JavaPlugin {

    private static UltimateSpellSystem instance;

    @Override
    public void onEnable() {
        instance = this;

    }

    @Override
    public void onDisable() {

    }

    public static void runTaskLater(Runnable runnable, long ticks) {
        Bukkit.getScheduler().runTaskLater(instance, runnable, ticks);
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
}
