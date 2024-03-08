package fr.jamailun.ultimatespellsystem.bukkit.runner.errors;

public class UnknownFunctionException extends UssRuntimeException {

    public UnknownFunctionException(String functionId) {
        super("Function '" + functionId + "' has not been defined.");
    }
}
