package fr.jamailun.ultimatespellsystem.dsl.errors;

import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenPosition;

public class TreeValidationException extends UssException{
    public TreeValidationException(TokenPosition pos, String message) {
        super(pos, message);
    }
}
