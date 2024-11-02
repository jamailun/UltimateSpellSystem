package fr.jamailun.ultimatespellsystem.bukkit.runner.errors;

public class InvalidTypeException extends UssRuntimeException {

    public InvalidTypeException(String context, String expected, Object obtained) {
        super("Invalid type on " + context + ". Expected " + expected+", got " + (obtained==null ? "null" : obtained + " | " + obtained.getClass()));
    }
}
