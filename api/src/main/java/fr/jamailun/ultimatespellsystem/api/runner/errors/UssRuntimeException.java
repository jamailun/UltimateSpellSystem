package fr.jamailun.ultimatespellsystem.api.runner.errors;

public abstract class UssRuntimeException extends RuntimeException {
    public UssRuntimeException(String msg) {
        super(msg);
    }
}
