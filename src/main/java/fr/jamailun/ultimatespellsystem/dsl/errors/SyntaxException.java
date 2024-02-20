package fr.jamailun.ultimatespellsystem.dsl.errors;

import fr.jamailun.ultimatespellsystem.dsl.tokenization.Token;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenPosition;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenType;

import java.util.List;

public class SyntaxException extends RuntimeException {

    public SyntaxException(TokenPosition position, String message) {
        super("SyntaxException at " + position + " : " + message);
    }

    public SyntaxException(Token token, String message) {
        super("SyntaxException with "+token+" at " + token.pos() + " : " + message);
    }

    public SyntaxException(Token token, TokenType expected) {
        super("SyntaxException with "+token+" at " + token.pos() + " : Expected a " + expected + ".");
    }
    public SyntaxException(Token token, List<TokenType> expected) {
        super("SyntaxException with "+token+" at " + token.pos() + " : Expected " + expected + ".");
    }

}
