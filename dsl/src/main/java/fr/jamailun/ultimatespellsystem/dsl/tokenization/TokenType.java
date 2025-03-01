package fr.jamailun.ultimatespellsystem.dsl.tokenization;

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

    // MonoOperators
    OPE_NOT, // !

    // Others
    ANTISLASH, // \
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
    PROPERTY_OPEN, // {{
    PROPERTY_CLOSE, // }}
    ARRAY_OPEN, // [[
    ARRAY_CLOSE, // ]]
    CHAR_AT, // @

    // == BI-CHAR OPERATORS

    COMP_LE, // <=
    COMP_GE, // >=
    COMP_EQ, // ==
    COMP_NE, // !=
    OPE_AND, // '&&'
    OPE_OR,  // '||'

    LIST_ADD, // :+
    LIST_REM, // :-
    LIST_REM_INDEX, // :/
    LIST_CONTAINS, // :?

    // == KEYWORDS

    STOP(true),
    IF(true),
    ELSE(true),
    FOR(true),
    WHILE(true),
    DO(true),

    DEFINE(true),
    SEND(true),
    TO(true),
    ALL(true),
    INCLUDING(true),
    AROUND(true),
    WITHIN(true),
    RUN(true),
    AFTER(true),
    TIMES(true),
    REPEAT(true),
    EVERY(true),
    SUMMON(true),
    AT(true),
    AS(true),
    WITH(true),
    POSITION(true),
    OF(true),
    TELEPORT(true),
    FOREACH(true),
    PLAY(true),
    CALL(true),
    SIZEOF(true),
    GIVE(true),

    INCREMENT(true),
    DECREMENT(true),

    // == RAW VALUES
    TRUE(true),
    FALSE(true),
    NULL(true),

    // == N-CHARS OPERATORS

    IDENTIFIER, // any combination of character that is NOT a string
    VALUE_VARIABLE, // a WORD starting with a '%'
    VALUE_STRING, // combination of characters between quotes
    VALUE_NUMBER,
    VALUE_DURATION, // number+ [s/ms/m/h/seconds]

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
                this == TRUE ||
                this == FALSE;
    }

}
