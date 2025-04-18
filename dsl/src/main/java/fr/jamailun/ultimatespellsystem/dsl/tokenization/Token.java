package fr.jamailun.ultimatespellsystem.dsl.tokenization;

import com.google.common.base.Preconditions;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

/**
 * Internal representation of a token.
 */
public class Token {

    @Getter private final TokenType type;
    private final TokenPosition position;

    private Double contentNumber;
    private String contentString;
    private TimeUnit contentTimeUnit;

    /**
     * New token.
     * @param type type of the token.
     * @param position position.
     */
    public Token(@NotNull TokenType type, @NotNull TokenPosition position) {
        this.type = type;
        this.position = position;
    }

    /**
     * New token from a raw string value.
     * @param string textual content.
     * @param position token position.
     * @return a new instance.
     */
    public static @NotNull Token fromString(@NotNull String string, @NotNull TokenPosition position) {
        Token token = new Token(TokenType.VALUE_STRING, position);
        token.contentString = string;
        return token;
    }

    /**
     * New token from a variable  value.
     * @param string textual content.
     * @param position token position.
     * @return a new instance.
     */
    public static @NotNull Token fromVariable(@NotNull String string, @NotNull TokenPosition position) {
        Token token = new Token(TokenType.VALUE_VARIABLE, position);
        token.contentString = string;
        return token;
    }

    /**
     * New token from a raw identifier.
     * @param string textual content.
     * @param position token position.
     * @return a new instance.
     */
    public static @NotNull Token fromWord(@NotNull String string, @NotNull TokenPosition position) {
        Token token = new Token(TokenType.IDENTIFIER, position);
        token.contentString = string;
        return token;
    }

    /**
     * New token from a  raw numerical value.
     * @param number numerical content.
     * @param position token position.
     * @return a new instance.
     */
    public static @NotNull Token fromNumber(double number, @NotNull TokenPosition position) {
        Token token = new Token(TokenType.VALUE_NUMBER, position);
        token.contentNumber = number;
        return token;
    }

    /**
     * New token from a raw duration value.
     * @param number numerical value.
     * @param tu time-unit value.
     * @param position token position.
     * @return a new instance.
     */
    public static @NotNull Token fromDuration(double number, @NotNull TimeUnit tu, @NotNull TokenPosition position) {
        Token token = new Token(TokenType.VALUE_DURATION, position);
        token.contentNumber = number;
        token.contentTimeUnit = tu;
        return token;
    }

    /**
     * Get the boolean content, if it exists.
     * @return a boolean value.
     */
    public boolean getContentBoolean() {
        return switch (type) {
            case TRUE -> true;
            case FALSE -> false;
            default -> throw new RuntimeException("Cannot get the BOOL content of type " + type);
        };
    }

    /**
     * Get the numerical content, if it exists.
     * @return a numerical value.
     */
    public @NotNull Double getContentNumber() {
        Preconditions.checkState(type == TokenType.VALUE_NUMBER || type == TokenType.VALUE_DURATION, "Cannot get the TIME_UNIT content of type " + type);
        return contentNumber;
    }

    /**
     * Get the time-unit content, if it exists.
     * @return a time-unit value.
     */
    public @NotNull TimeUnit getContentTimeUnit() {
        Preconditions.checkState(type == TokenType.VALUE_DURATION, "Cannot get the TIME_UNIT content of type " + type);
        return contentTimeUnit;
    }

    /**
     * Get the textual content, if it exists.
     * @return a textual value.
     */
    public @NotNull String getContentString() {
        Preconditions.checkState(type == TokenType.VALUE_STRING || type == TokenType.VALUE_VARIABLE || type == TokenType.IDENTIFIER, "Cannot get the STRING content of type " + type);
        return contentString;
    }

    /**
     * Get the token position.
     * @return a non-null position instance.
     */
    public @NotNull TokenPosition pos() {
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
