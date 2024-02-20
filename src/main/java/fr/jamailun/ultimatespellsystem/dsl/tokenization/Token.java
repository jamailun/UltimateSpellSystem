package fr.jamailun.ultimatespellsystem.dsl.tokenization;

public class Token {

    private final TokenType type;
    private final TokenPosition position;

    private Double contentNumber;
    private String contentString;

    public Token(TokenType type, TokenPosition position) {
        this.type = type;
        this.position = position;
    }

    public static Token fromString(String string, TokenPosition position) {
        Token token = new Token(TokenType.STRING, position);
        token.contentString = string;
        return token;
    }

    public static Token fromVariable(String string, TokenPosition position) {
        Token token = new Token(TokenType.VARIABLE, position);
        token.contentString = string;
        return token;
    }

    public static Token fromWord(String string, TokenPosition position) {
        Token token = new Token(TokenType.WORD, position);
        token.contentString = string;
        return token;
    }

    public static Token fromNumber(double number, TokenPosition position) {
        Token token = new Token(TokenType.NUMBER, position);
        token.contentNumber = number;
        return token;
    }

    public TokenType getType() {
        return type;
    }

    public boolean getContentBoolean() {
        return switch (type) {
            case TRUE -> true;
            case FALSE -> false;
            default -> throw new RuntimeException("Cannot get the BOOL content of type " + type);
        };
    }

    public Double getContentNumber() {
        if(type != TokenType.NUMBER)
            throw new RuntimeException("Cannot get the NUMBER content of type " + type);
        return contentNumber;
    }

    public String getContentString() {
        if(type != TokenType.STRING && type != TokenType.VARIABLE)
            throw new RuntimeException("Cannot get the STRING content of type " + type);
        return contentString;
    }

    public TokenPosition pos() {
        return position;
    }

    @Override
    public String toString() {
        if(type == TokenType.WORD) {
            return type + "[" + contentString + "]";
        }
        if(type == TokenType.VARIABLE || type == TokenType.STRING) {
            return type + "(\"" + contentString + "\")";
        }
        if(type == TokenType.NUMBER) {
            return type + "(" + contentNumber + ")";
        }
        return type.name();
    }
}
