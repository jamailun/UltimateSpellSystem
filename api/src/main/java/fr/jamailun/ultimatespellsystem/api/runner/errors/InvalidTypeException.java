package fr.jamailun.ultimatespellsystem.api.runner.errors;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Invalid USS typing. <br/>
 * This is a <b>user</b> exception.
 */
public class InvalidTypeException extends UssRuntimeException {

    /**
     * Create a new exception.
     * @param context the textual context.
     * @param expected the expected type.
     * @param obtained the obtained object.
     */
    public InvalidTypeException(@NotNull String context, @NotNull String expected, @Nullable Object obtained) {
        super("Invalid type on " + context + ". Expected " + expected+", got " + (obtained==null ? "null" : obtained + " | " + obtained.getClass()));
    }

    /**
     * Create a new exception.
     * @param context the textual context.
     * @param error the explicit error to use.
     */
    public InvalidTypeException(@NotNull String context, @NotNull String error) {
        super("Invalid type on " + context + ". " + error);
    }
}
