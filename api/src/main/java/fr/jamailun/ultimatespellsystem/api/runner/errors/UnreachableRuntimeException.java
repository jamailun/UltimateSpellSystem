package fr.jamailun.ultimatespellsystem.api.runner.errors;

/**
 * Exception to throw when a non-reachable code as been reached.
 */
public class UnreachableRuntimeException extends UssRuntimeException {

    /**
     * Create a new instance.
     * @param message error message.
     */
    public UnreachableRuntimeException(String message) {
        super(message);
    }
}
