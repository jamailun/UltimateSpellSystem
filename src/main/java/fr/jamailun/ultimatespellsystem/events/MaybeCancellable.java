package fr.jamailun.ultimatespellsystem.events;

import org.bukkit.event.Cancellable;

public interface MaybeCancellable extends Cancellable {
    boolean isCancellable();
}
