package fr.jamailun.ultimatespellsystem.dsl;

public class Token {

    private final TokenType type;
    private final TokenPosition position;

    private Boolean contentBoolean;
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
        if(string.equals("true") || string.equals("false")) {
            return fromBoolean(Boolean.parseBoolean(string), position);
        }

        Token token = new Token(TokenType.WORD, position);
        token.contentString = string;
        return token;
    }

    public static Token fromNumber(double number, TokenPosition position) {
        Token token = new Token(TokenType.NUMBER, position);
        token.contentNumber = number;
        return token;
    }

    public static Token fromBoolean(boolean bool, TokenPosition position) {
        Token token = new Token(TokenType.BOOLEAN, position);
        token.contentBoolean = bool;
        return token;
    }


    public Boolean getContentBoolean() {
        if(type != TokenType.BOOLEAN)
            throw new RuntimeException("Cannot get the BOOLEAN content of type " + type);
        return contentBoolean;
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
        if(type == TokenType.BOOLEAN) {
            return type + "(" + contentBoolean + ")";
        }
        if(type == TokenType.NUMBER) {
            return type + "(" + contentNumber + ")";
        }
        return type.name();
    }
}
