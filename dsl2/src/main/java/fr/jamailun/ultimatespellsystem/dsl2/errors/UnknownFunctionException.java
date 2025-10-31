package fr.jamailun.ultimatespellsystem.dsl2.errors;

import fr.jamailun.ultimatespellsystem.dsl2.tokenization.TokenPosition;
import org.jetbrains.annotations.NotNull;

/**
 * An exception threw when a function has not been registered.
 */
public class UnknownFunctionException extends UssException {

    /**
     * New exception.
     * @param pos token position.
     * @param functionId ID of the unknown function.
     */
    public UnknownFunctionException(@NotNull TokenPosition pos, @NotNull String functionId) {
        super(pos, "Unknown function ID: '"+functionId+"'.");
    }

    /**
     * New exception for a struct.
     * @param pos token position.
     * @param structName name of the structure.
     * @param functionId ID of the unknown function.
     */
    public UnknownFunctionException(@NotNull TokenPosition pos, @NotNull String structName, @NotNull String functionId) {
        super(pos, "Unknown function ID: '"+functionId+"' in struct " + structName + ".");
    }
}
