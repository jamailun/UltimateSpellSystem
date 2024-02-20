package fr.jamailun.ultimatespellsystem.dsl.tokenization;

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

    // KEYWORDS

    IF,
    FOR,
    STOP,
    WHILE,
    DO,
    ELSE,
    SECTION,
    SEND,
    MESSAGE,
    EFFECT,
    TO,

    // variables
    TRUE,
    FALSE,
    NULL,

    // N-CHARS OPERATORS

    IDENTIFIER, // any combination of character that is NOT a string
    VALUE_VARIABLE, // a WORD starting with a '%'
    VALUE_STRING, // combination of characters between quotes
    VALUE_NUMBER,
    VALUE_DURATION, // number+ [s/ms/m/h/seconds]


    // END OF FILE
    EOF;

    public Token toToken(TokenPosition position) {
        return new Token(this, position);
    }

}
