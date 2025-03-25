package fr.jamailun.ultimatespellsystem.api.runner.errors;

/**
 * Exception thrown when a function name has not been registered.
 * @see fr.jamailun.ultimatespellsystem.api.providers.JavaFunctionProvider JavaFunctionProvider
 */
public class UnknownFunctionException extends UssRuntimeException {

    /**
     * Create a new instance with a function name.
     * @param functionId name of the function that could not be found.
     */
    public UnknownFunctionException(String functionId) {
        super("Function '" + functionId + "' has not been defined.");
    }
}
