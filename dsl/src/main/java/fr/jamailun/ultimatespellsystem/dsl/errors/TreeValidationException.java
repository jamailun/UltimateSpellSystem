package fr.jamailun.ultimatespellsystem.dsl.errors;

import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenPosition;

/**
 * Tree validation : relation between statements.
 */
public class TreeValidationException extends UssException {

    /**
     * Tree validation failure.
     * @param pos token position.
     * @param message error description.
     */
    public TreeValidationException(TokenPosition pos, String message) {
        super(pos, message);
    }
}
