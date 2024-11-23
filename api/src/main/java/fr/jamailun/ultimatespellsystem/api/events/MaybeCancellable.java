package fr.jamailun.ultimatespellsystem.api.events;

import org.bukkit.event.Cancellable;

public interface MaybeCancellable extends Cancellable {
    boolean isCancellable();
}
