package fr.jamailun.ultimatespellsystem.dsl2.tokenization;

/**
 * Type of token.
 */
public enum TokenType {

    // == MONO-CHAR OPERATORS

    // BiOperators
    OPE_ADD, // +
    OPE_SUB, // -
    OPE_MUL, // *
    OPE_DIV, // /
    OPE_MOD, // %

    // MonoOperators
    OPE_NOT, // !
    INCREMENT, // ++
    DECREMENT, // --

    // Others
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

    // == BI-CHAR OPERATORS

    COMP_LE, // <=
    COMP_GE, // >=
    COMP_EQ, // ==
    COMP_NE, // !=
    OPE_AND, // '&&'
    OPE_OR,  // '||'

    // == KEYWORDS
    CHAR_AT, // @

    IF(true),
    ELSE(true),
    FOR(true),
    WHILE(true),
    DO(true),
    BREAK(true),
    CONTINUE(true),
    RETURN(true),
    VAR(true),

    // == RAW VALUES
    NULL(true),

    // == N-CHARS OPERATORS

    IDENTIFIER, // any combination of character that is NOT a string
    VALUE_STRING, // combination of characters between quotes
    VALUE_NUMBER,
    VALUE_DURATION, // number+ [s/ms/m/h/seconds]
    VALUE_BOOLEAN, // true/false

    // == END OF FILE
    EOF;

    public final boolean letters;

    TokenType() {
        this(false);
    }
    TokenType(boolean letters) {
        this.letters = letters;
    }

    public Token toToken(TokenPosition position) {
        return new Token(this, position);
    }

    public boolean isRawValue() {
        return  this == IDENTIFIER ||
                this == VALUE_STRING ||
                this == VALUE_NUMBER ||
                this == VALUE_DURATION ||
                this == VALUE_BOOLEAN ;
    }

}
