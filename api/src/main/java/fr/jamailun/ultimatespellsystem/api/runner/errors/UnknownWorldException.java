package fr.jamailun.ultimatespellsystem.api.runner.errors;

public class UnknownWorldException extends UssRuntimeException {
    public UnknownWorldException(String worldName) {
        super("Unknown worldName: '" + worldName + "'.");
    }
}
