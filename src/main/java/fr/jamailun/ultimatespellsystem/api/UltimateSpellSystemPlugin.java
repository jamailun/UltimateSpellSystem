package fr.jamailun.ultimatespellsystem.api;

import fr.jamailun.ultimatespellsystem.api.bukkit.animations.AnimationsManager;
import fr.jamailun.ultimatespellsystem.api.bukkit.bind.ItemBinder;
import fr.jamailun.ultimatespellsystem.api.bukkit.entities.SummonsManager;
import fr.jamailun.ultimatespellsystem.api.bukkit.spells.SpellsManager;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public interface UltimateSpellSystemPlugin {

    void logDebug(@NotNull String message);

    void logInfo(@NotNull String message);

    void logWarning(@NotNull String message);

    void logError(@NotNull String message);

    @NotNull SpellsManager getSpellsManager();

    @NotNull SummonsManager getSummonsManager();

    @NotNull ItemBinder getItemBinder();

    @NotNull AnimationsManager getAnimationsManager();

    @NotNull BukkitRunnable runTaskLater(@NotNull Runnable runnable, long ticks);

    @NotNull BukkitRunnable runTaskRepeat(@NotNull Runnable runnable, int amount, long delay, long period);

    @NotNull BukkitRunnable runTaskRepeat(Runnable runnable, long delay, long period);

    void reloadConfiguration();

}
