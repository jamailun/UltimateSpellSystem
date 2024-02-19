package fr.jamailun.ultimatespellsystem.dsl.errors;

import fr.jamailun.ultimatespellsystem.dsl.TokenPosition;

public class ParsingException extends RuntimeException {

    public ParsingException(TokenPosition pos, char c, String message) {
        super("Unexpected char at " + pos + ": '" + c + "'. " + message);
    }

    public ParsingException(TokenPosition pos, String message) {
        super("Unexpected char at " + pos + ": " + message);
    }

}
