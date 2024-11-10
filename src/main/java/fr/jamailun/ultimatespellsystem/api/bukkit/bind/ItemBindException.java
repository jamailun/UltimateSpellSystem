package fr.jamailun.ultimatespellsystem.api.bukkit.bind;

import org.jetbrains.annotations.NotNull;

/**
 * An exception threw when an item could not be bind.
 */
public final class ItemBindException extends Exception {

    /**
     * Create a new exception.
     * @param message the message to use.
     */
    public ItemBindException(@NotNull String message) {
        super(message);
    }

}
