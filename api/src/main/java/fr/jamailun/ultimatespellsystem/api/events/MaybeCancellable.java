package fr.jamailun.ultimatespellsystem.api.events;

import org.bukkit.event.Cancellable;

/**
 * An event that <b>may</b> be cancelled.
 */
public interface MaybeCancellable extends Cancellable {

    /**
     * Tets if this event can bve cancelled.
     * @return true if a call to {@link #setCancelled(boolean)} will do something.
     */
    boolean isCancellable();
}
