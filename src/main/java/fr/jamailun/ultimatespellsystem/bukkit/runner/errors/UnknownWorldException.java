package fr.jamailun.ultimatespellsystem.bukkit.runner.errors;

public class UnknownWorldException extends UssRuntimeException {
    public UnknownWorldException(String worldName) {
        super("Unknown worldName: '" + worldName + "'.");
    }
}
