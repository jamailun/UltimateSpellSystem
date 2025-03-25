package fr.jamailun.ultimatespellsystem.api.runner.errors;

import org.jetbrains.annotations.NotNull;

/**
 * Abstract exception for USS issues.
 */
public abstract class UssRuntimeException extends RuntimeException {

    /**
     * Create a new exception instance, with a message.
     * @param msg a non-null message to throw.
     */
    protected UssRuntimeException(@NotNull String msg) {
        super(msg);
    }
}
