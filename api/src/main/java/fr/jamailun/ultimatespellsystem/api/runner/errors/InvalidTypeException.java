package fr.jamailun.ultimatespellsystem.api.runner.errors;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class InvalidTypeException extends UssRuntimeException {

    public InvalidTypeException(@NotNull String context, @NotNull String expected, @Nullable Object obtained) {
        super("Invalid type on " + context + ". Expected " + expected+", got " + (obtained==null ? "null" : obtained + " | " + obtained.getClass()));
    }
    public InvalidTypeException(@NotNull String context, @NotNull String error) {
        super("Invalid type on " + context + ". " + error);
    }
}
