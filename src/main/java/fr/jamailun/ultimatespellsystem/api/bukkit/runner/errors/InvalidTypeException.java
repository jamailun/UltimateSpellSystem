package fr.jamailun.ultimatespellsystem.api.bukkit.runner.errors;

public class InvalidTypeException extends UssRuntimeException {

    public InvalidTypeException(String context, String expected, Object obtained) {
        super("Invalid type on " + context + ". Expected " + expected+", got " + (obtained==null ? "null" : obtained + " | " + obtained.getClass()));
    }
    public InvalidTypeException(String context, String error) {
        super("Invalid type on " + context + ". " + error);
    }
}
