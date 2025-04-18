package fr.jamailun.ultimatespellsystem;

import fr.jamailun.ultimatespellsystem.api.utils.Scheduler;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * Internal access to Bukkit schedule, for the plugin.
 */
public final class UssScheduler implements Scheduler {

    private final Plugin plugin;
    UssScheduler(@NotNull Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull BukkitRunnable run(@NotNull Runnable runnable) {
        return of(runnable, t -> t.runTask(plugin));
    }

    @Override
    public @NotNull BukkitRunnable runAsync(@NotNull Runnable runnable) {
        return of(runnable, t -> t.runTaskAsynchronously(plugin));
    }

    @Override
    public @NotNull BukkitRunnable runTaskLater(@NotNull Runnable runnable, long ticks) {
        return of(runnable, t -> t.runTaskLater(plugin, ticks));
    }

    @Override
    public @NotNull BukkitRunnable runTaskLaterAsync(@NotNull Runnable runnable, long ticks) {
        return of(runnable, t -> t.runTaskLaterAsynchronously(plugin, ticks));
    }

    public @NotNull BukkitRunnable runTaskRepeat(@NotNull Runnable runnable, int amount, long delay, long period) {
        BukkitRunnable br = specificRepeat(runnable, amount);
        br.runTaskTimer(plugin, delay, period);
        return br;
    }

    @Override
    public @NotNull BukkitRunnable runTaskRepeatAsync(@NotNull Runnable runnable, int amount, long delay, long period) {
        BukkitRunnable br = specificRepeat(runnable, amount);
        br.runTaskTimerAsynchronously(plugin, delay, period);
        return br;
    }

    public @NotNull BukkitRunnable runTaskRepeat(Runnable runnable, long delay, long period) {
        return of(runnable, t -> t.runTaskTimer(plugin, delay, period));
    }

    @Override
    public @NotNull BukkitRunnable runTaskRepeatAsync(Runnable runnable, long delay, long period) {
        return of(runnable, t -> t.runTaskTimerAsynchronously(plugin, delay, period));
    }

    // ----

    private @NotNull BukkitRunnable of(@NotNull Runnable runnable, @NotNull Consumer<BukkitRunnable> consumer) {
        BukkitRunnable t = new BukkitRunnable() {public void run() {runnable.run();}};
        consumer.accept(t);
        return t;
    }

    @Contract(value = "_, _ -> new", pure = true)
    private @NotNull BukkitRunnable specificRepeat(@NotNull Runnable runnable, int amount) {
        return new BukkitRunnable() {
            private int count = 0;
            @Override
            public void run() {
                runnable.run();
                count++;
                if(count == amount)
                    cancel();
            }
        };
    }

}
