package fr.jamailun.ultimatespellsystem.dsl.tokenization;

import lombok.Getter;

import java.util.concurrent.TimeUnit;

public class Token {

    @Getter private final TokenType type;
    private final TokenPosition position;

    private Double contentNumber;
    private String contentString;
    private TimeUnit contentTimeUnit;

    public Token(TokenType type, TokenPosition position) {
        this.type = type;
        this.position = position;
    }

    public static Token fromString(String string, TokenPosition position) {
        Token token = new Token(TokenType.VALUE_STRING, position);
        token.contentString = string;
        return token;
    }

    public static Token fromVariable(String string, TokenPosition position) {
        Token token = new Token(TokenType.VALUE_VARIABLE, position);
        token.contentString = string;
        return token;
    }

    public static Token fromWord(String string, TokenPosition position) {
        Token token = new Token(TokenType.IDENTIFIER, position);
        token.contentString = string;
        return token;
    }

    public static Token fromNumber(double number, TokenPosition position) {
        Token token = new Token(TokenType.VALUE_NUMBER, position);
        token.contentNumber = number;
        return token;
    }
    public static Token fromDuration(double number, TimeUnit tu, TokenPosition position) {
        Token token = new Token(TokenType.VALUE_DURATION, position);
        token.contentNumber = number;
        token.contentTimeUnit = tu;
        return token;
    }

    public boolean getContentBoolean() {
        return switch (type) {
            case TRUE -> true;
            case FALSE -> false;
            default -> throw new RuntimeException("Cannot get the BOOL content of type " + type);
        };
    }

    public Double getContentNumber() {
        if(type != TokenType.VALUE_NUMBER && type != TokenType.VALUE_DURATION)
            throw new RuntimeException("Cannot get the NUMBER content of type " + type);
        return contentNumber;
    }
    public TimeUnit getContentTimeUnit() {
        if(type != TokenType.VALUE_DURATION)
            throw new RuntimeException("Cannot get the TIME_UNIT content of type " + type);
        return contentTimeUnit;
    }

    public String getContentString() {
        if(type != TokenType.VALUE_STRING && type != TokenType.VALUE_VARIABLE && type != TokenType.IDENTIFIER)
            throw new RuntimeException("Cannot get the STRING content of type " + type);
        return contentString;
    }

    public TokenPosition pos() {
        return position;
    }

    @Override
    public String toString() {
        if(type == TokenType.IDENTIFIER) {
            return "[" + contentString + "]";
        }
        if(type == TokenType.VALUE_DURATION) {
            return type + "(" + contentNumber + " " + contentTimeUnit + ")";
        }
        if(type == TokenType.VALUE_VARIABLE || type == TokenType.VALUE_STRING) {
            return type + "(\"" + contentString + "\")";
        }
        if(type == TokenType.VALUE_NUMBER) {
            return type + "(" + contentNumber + ")";
        }
        return type.name();
    }
}
