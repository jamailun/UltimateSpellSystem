package fr.jamailun.ultimatespellsystem.dsl;

public enum TokenType {

    // MONO-CHAR OPERATORS

    SLASH, // /
    ANTISLASH, // \
    PLUS, // +
    MINUS, // -
    TIMES, // *
    NOT, // !
    COLON, // :
    SEMI_COLON, // ;
    COMMA, // ,
    DOT, // .
    EQUAL, // =
    BRACKET_OPEN,  // (
    BRACKET_CLOSE, // )
    SQUARE_BRACKET_OPEN,  // [
    SQUARE_BRACKET_CLOSE, // ]
    BRACES_OPEN, // {
    BRACES_CLOSE, // }
    COMP_LT, // <
    COMP_GT, // >

    // BI-CHAR OPERATORS

    COMP_LE, // <=
    COMP_GE, // >=
    COMP_EQ, // ==
    COMP_NE, // !=

    // N-CHARS OPERATORS

    WORD, // any combination of character that is NOT a string
    VARIABLE, // a WORD starting with a '%'
    STRING, // combination of characters between quotes
    NUMBER,
    BOOLEAN,


    // END OF FILE
    EOF;

    public Token toToken(TokenPosition position) {
        return new Token(this, position);
    }

}
