package fr.jamailun.ultimatespellsystem.bukkit.events;

import org.bukkit.event.Cancellable;

public interface MaybeCancellable extends Cancellable {
    boolean isCancellable();
}
