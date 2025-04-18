package fr.jamailun.ultimatespellsystem.dsl.errors;

import fr.jamailun.ultimatespellsystem.dsl.tokenization.Token;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenPosition;
import org.jetbrains.annotations.NotNull;

/**
 * Abstract exception for USS related problems.
 * Provides access to the position in the source.
 */
public abstract class UssException extends RuntimeException {

    /**
     * New instance with a token position only.
     * @param pos token position.
     * @param message exception details.
     */
    public UssException(@NotNull TokenPosition pos, @NotNull String message) {
        super("At " + pos + ". " + message);
    }

    /**
     * New instance with a real token instance.
     * @param token token source of the exception.
     * @param message exception details.
     */
    public UssException(@NotNull Token token, @NotNull String message) {
        super("With " + token + " at " + token.pos() + ". " + message);
    }

}
