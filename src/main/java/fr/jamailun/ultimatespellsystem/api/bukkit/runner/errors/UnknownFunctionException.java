package fr.jamailun.ultimatespellsystem.api.bukkit.runner.errors;

public class UnknownFunctionException extends UssRuntimeException {

    public UnknownFunctionException(String functionId) {
        super("Function '" + functionId + "' has not been defined.");
    }
}
