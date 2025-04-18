package fr.jamailun.ultimatespellsystem.api.utils;

import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

/**
 * Schedule tasks inside the USS context. This will allow the purge to properly cleanup elements.
 */
public interface Scheduler {

  /**
   * Run a task on the main.
   * @param runnable thing to run
   * @return a non-null {@link BukkitRunnable}.
   */
  @NotNull BukkitRunnable run(@NotNull Runnable runnable);

  /**
   * Run a task on a secondary thread.
   * @param runnable thing to run
   * @return a non-null {@link BukkitRunnable}.
   */
  @NotNull BukkitRunnable runAsync(@NotNull Runnable runnable);

  /**
   * Run a task on the main thread, later.
   * @param runnable thing to run.
   * @param ticks delay.
   * @return a non-null {@link BukkitRunnable}.
   */
  @NotNull BukkitRunnable runTaskLater(@NotNull Runnable runnable, long ticks);

  /**
   * Run a task on a secondary thread, later.
   * @param runnable thing to run.
   * @param ticks delay.
   * @return a non-null {@link BukkitRunnable}.
   */
  @NotNull BukkitRunnable runTaskLaterAsync(@NotNull Runnable runnable, long ticks);

  /**
   * Repeat a task on the main thread, a specific amount of times.
   * @param runnable thing to run.
   * @param amount number of times to execute the task before cancellation.
   * @param delay initial delay.
   * @param period period of execution.
   * @return a non-null {@link BukkitRunnable}.
   */
  @NotNull BukkitRunnable runTaskRepeat(@NotNull Runnable runnable, int amount, long delay, long period);

  /**
   * Repeat a task a specific amount of times on a secondary thread.
   * @param runnable thing to run.
   * @param amount number of times to execute the task before cancellation.
   * @param delay initial delay.
   * @param period period of execution.
   * @return a non-null {@link BukkitRunnable}.
   */
  @NotNull BukkitRunnable runTaskRepeatAsync(@NotNull Runnable runnable, int amount, long delay, long period);

  /**
   * Repeat a task on the main thread.
   * @param runnable thing to run.
   * @param delay initial delay.
   * @param period period of execution.
   * @return a non-null {@link BukkitRunnable}.
   */
  @NotNull BukkitRunnable runTaskRepeat(Runnable runnable, long delay, long period);

  /**
   * Repeat a task on a secondary thread.
   * @param runnable thing to run.
   * @param delay initial delay.
   * @param period period of execution.
   * @return a non-null {@link BukkitRunnable}.
   */
  @NotNull BukkitRunnable runTaskRepeatAsync(Runnable runnable, long delay, long period);

}
