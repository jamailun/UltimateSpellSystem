package fr.jamailun.ultimatespellsystem.dsl.errors;

import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenPosition;
import org.jetbrains.annotations.NotNull;

/**
 * An exception threw when a function has not been registered.
 */
public class UnknownFunctionException extends UssException {
    public UnknownFunctionException(@NotNull TokenPosition pos, @NotNull String functionId) {
        super(pos, "Unknown function ID: '"+functionId+"'.");
    }
}
