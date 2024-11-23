package fr.jamailun.ultimatespellsystem.dsl.errors;

import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenPosition;

public class ParsingException extends UssException {

    public ParsingException(TokenPosition pos, char c, String message) {
        super(pos, "Unexpected character : '" + c + "'. " + message);
    }

    public ParsingException(TokenPosition pos, String message) {
        super(pos, "Unexpected character. " + message);
    }

}
