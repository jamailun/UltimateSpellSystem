package fr.jamailun.ultimatespellsystem.dsl2.errors;

import fr.jamailun.ultimatespellsystem.dsl2.tokenization.Token;
import fr.jamailun.ultimatespellsystem.dsl2.tokenization.TokenPosition;
import fr.jamailun.ultimatespellsystem.dsl2.tokenization.TokenType;

import java.util.List;

/**
 * Syntax exception : the spell has a user error.
 */
public class SyntaxException extends UssException {

    /**
     * Standard syntax exception.
     * @param position token position.
     * @param message exception description.
     */
    public SyntaxException(TokenPosition position, String message) {
        super(position, message);
    }

    /**
     * Standard syntax exception.
     * @param token token source of the error.
     * @param message exception description.
     */
    public SyntaxException(Token token, String message) {
        super(token, message);
    }

    /**
     * Bad token exception.
     * @param token problematic token.
     * @param expected expected token type.
     */
    public SyntaxException(Token token, TokenType expected) {
        super(token, "Expected a " + expected + ".");
    }

    /**
     * Bad token exception.
     * @param token problematic token.
     * @param expected expected token type list (OR).
     */
    public SyntaxException(Token token, List<TokenType> expected) {
        super(token, "Expected one of: " + expected + ".");
    }

}
