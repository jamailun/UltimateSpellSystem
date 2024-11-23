package fr.jamailun.ultimatespellsystem.dsl.errors;

import fr.jamailun.ultimatespellsystem.dsl.tokenization.Token;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenPosition;

/**
 * Abstract exception for USS related problems.
 * Provides access to the position in the source.
 */
public abstract class UssException extends RuntimeException {

    public UssException(TokenPosition pos, String message) {
        super("At " + pos + ". " + message);
    }

    public UssException(Token token, String message) {
        super("With " + token + " at " + token.pos() + ". " + message);
    }

}
