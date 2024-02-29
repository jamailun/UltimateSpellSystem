package fr.jamailun.ultimatespellsystem.dsl.errors;

import fr.jamailun.ultimatespellsystem.dsl.tokenization.Token;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenPosition;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenType;

import java.util.List;

public class SyntaxException extends UssException {

    public SyntaxException(TokenPosition position, String message) {
        super(position, message);
    }

    public SyntaxException(Token token, String message) {
        super(token, message);
    }

    public SyntaxException(Token token, TokenType expected) {
        super(token, "Expected a " + expected + ".");
    }
    public SyntaxException(Token token, List<TokenType> expected) {
        super(token, "Expected one of: " + expected + ".");
    }

}
