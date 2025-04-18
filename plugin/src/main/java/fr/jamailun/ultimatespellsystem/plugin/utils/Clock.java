package fr.jamailun.ultimatespellsystem.plugin.utils;

import fr.jamailun.ultimatespellsystem.UssScheduler;
import fr.jamailun.ultimatespellsystem.api.UltimateSpellSystem;
import lombok.Getter;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

/**
 * A clock is a runtime-configurable bukkit task.
 */
public class Clock {

    private final Runnable runnable;
    private BukkitRunnable task;

    @Getter private double frequency;
    private boolean pause;

    public Clock(@NotNull Runnable runnable) {
        this(runnable, -1d);
    }

    public Clock(@NotNull Runnable runnable, double frequency) {
        this.runnable = runnable;
        setFrequency(frequency);
    }

    /**
     * Change the frequency of the clock.
     * @param value the frequency to use. If null or negative, clock will not start.
     */
    public void setFrequency(double value) {
        frequency = value <= 0 ? -1 : value;
        // Restart ?
        stop();
        if(frequency > 0) {
            start();
        }
    }

    private void stop() {
        if(task != null) {
            task.cancel();
            task = null;
        }
    }

    private void start() {
         task = UltimateSpellSystem.getScheduler().runTaskRepeat(runnable, 0, (long) (frequency * 20));
    }

    public boolean isPaused() {
        return pause;
    }

    public void setPaused(boolean paused) {
        this.pause = paused;
        if(pause)
            stop();
        else
            start();
    }

}
