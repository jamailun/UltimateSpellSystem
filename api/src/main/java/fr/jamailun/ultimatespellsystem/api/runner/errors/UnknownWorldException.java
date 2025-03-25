package fr.jamailun.ultimatespellsystem.api.runner.errors;

/**
 * Exception thrown when a world does not exist in a {@code location} expression.
 */
public class UnknownWorldException extends UssRuntimeException {

    /**
     * Create a new exception, from the world name.
     * @param worldName world name that could not be found.
     */
    public UnknownWorldException(String worldName) {
        super("Unknown worldName: '" + worldName + "'.");
    }
}
