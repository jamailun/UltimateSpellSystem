package fr.jamailun.ultimatespellsystem.dsl2.errors;

import fr.jamailun.ultimatespellsystem.dsl2.tokenization.TokenPosition;

/**
 * Exception thrown when a requested field does not exist.
 */
public class UnknownFieldException extends UssException {

    /**
     * New instance.
     * @param pos position of the token.
     * @param type type of the struct.
     * @param fieldName requested field name.
     */
    public UnknownFieldException(TokenPosition pos, String type, String fieldName) {
        super(pos, "The type " + type + " has not field '" + fieldName + "'.");
    }

}
