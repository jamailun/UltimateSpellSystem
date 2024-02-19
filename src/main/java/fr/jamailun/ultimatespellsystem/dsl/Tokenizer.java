package fr.jamailun.ultimatespellsystem.dsl;

import fr.jamailun.ultimatespellsystem.dsl.errors.ParsingException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class Tokenizer {

    private final static Map<Character, TokenType> OPERATORS_MONO = new HashMap<>();
    private final static Map<String, TokenType> OPERATORS_BI = new HashMap<>();
    static {
        OPERATORS_MONO.put('/', TokenType.SLASH);
        OPERATORS_MONO.put('\\', TokenType.ANTISLASH);
        OPERATORS_MONO.put('+', TokenType.PLUS);
        OPERATORS_MONO.put('-', TokenType.MINUS);
        OPERATORS_MONO.put('*', TokenType.TIMES);
        OPERATORS_MONO.put('=', TokenType.EQUAL);
        OPERATORS_MONO.put('!', TokenType.NOT);
        OPERATORS_MONO.put(':', TokenType.COLON);
        OPERATORS_MONO.put(';', TokenType.SEMI_COLON);
        OPERATORS_MONO.put(',', TokenType.COMMA);
        OPERATORS_MONO.put('.', TokenType.DOT);
        OPERATORS_MONO.put('(', TokenType.BRACKET_OPEN);
        OPERATORS_MONO.put(')', TokenType.BRACKET_CLOSE);
        OPERATORS_MONO.put('[', TokenType.SQUARE_BRACKET_OPEN);
        OPERATORS_MONO.put(']', TokenType.SQUARE_BRACKET_CLOSE);
        OPERATORS_MONO.put('{', TokenType.BRACES_OPEN);
        OPERATORS_MONO.put('}', TokenType.BRACES_CLOSE);
        OPERATORS_MONO.put('<', TokenType.COMP_LT);
        OPERATORS_MONO.put('>', TokenType.COMP_GT);

        OPERATORS_BI.put("<=", TokenType.COMP_LE);
        OPERATORS_BI.put(">=", TokenType.COMP_GE);
        OPERATORS_BI.put("==", TokenType.COMP_EQ);
        OPERATORS_BI.put("!=", TokenType.COMP_NE);
    }

    private final CharStream chars;
    private final List<Token> tokens = new ArrayList<>();
    private boolean done = false;

    public static TokenStream tokenize(CharStream chars) {
        return new Tokenizer(chars).tokenize();
    }


    private final Predicate<String> ALLOWED_WORD_START = Pattern.compile("[A-Za-z_]").asPredicate();
    private final Predicate<String> ALLOWED_WORD_BODY = Pattern.compile("[A-Za-z_0-9]").asPredicate();

    private Tokenizer(CharStream chars) {
        this.chars = chars;
    }

    private TokenStream tokenize() {
        if(done)
            return new TokenStream(tokens);
        done = true;

        while(chars.hasMore()) {
            char current = chars.next();

            // Whitespace-trash
            if(current == '\s' || current == '\t' || current == '\r' || current == '\n')
                continue;

            // Comment
            if(current == '#') {
                skipUntilEOL();
                continue;
            }

            // Basic symbols.
            // All bi-operators start with one of the mono... let's use this fact.
            if(OPERATORS_MONO.containsKey(current)) {
                if(chars.hasMore()) {
                    char next = chars.peek();

                    // Number ?
                    if(current == '.' && isDigit(next)) {
                        tokens.add(Token.fromNumber(getNumber(current), chars.pos()));
                        continue;
                    }

                    // Bi-operator ?
                    String symbol = String.valueOf(current) + next;
                    if(OPERATORS_BI.containsKey(symbol)) {
                        addRaw(OPERATORS_BI.get(symbol));
                        chars.drop();
                        continue;
                    }
                }
                addRaw(OPERATORS_MONO.get(current));
                continue;
            }

            // String
            if(current == '"') {
                addString();
                continue;
            }

            if(current == '%') {
                if( ! chars.hasMore()) {
                    throw new ParsingException(chars.pos(), current, "Cannot end file with a '%' : expected a variable name.");
                }
                tokens.add(Token.fromVariable(getWord(null), chars.pos()));
                continue;
            }

            if(ALLOWED_WORD_START.test(String.valueOf(current))) {
                tokens.add(Token.fromWord(getWord(current), chars.pos()));
                continue;
            }

            if(isDigit(current)) {
                tokens.add(Token.fromNumber(getNumber(current), chars.pos()));
                continue;
            }

            throw new ParsingException(chars.pos(), current, "Unknown character.");
        }

        addRaw(TokenType.EOF);
        return new TokenStream(tokens);
    }

    private double getNumber(char first) {
        StringBuilder sb = new StringBuilder();
        sb.append(first); // Can be a digit, a dot or a negative sign.

        boolean dot = first == '.';
        while(chars.hasMore()) {
            char c = chars.peek();

            // Dot
            if(c == '.') {
                if(dot)
                    throw new ParsingException(chars.pos(), c, "Cannot have multiple '.' in a number.");
                dot = true;
                sb.append('.');
                continue;
            }

            // Number
            if(isDigit(c)) {
                sb.append(c);
                continue;
            }

            // EON
            break;
        }
        return Double.parseDouble(sb.toString());
    }

    private void skipUntilEOL() {
        while(chars.hasMore()) {
            if(chars.next() == '\n') {
                break;
            }
        }
    }

    private String getWord(Character firstChar) {
        StringBuilder content = new StringBuilder();
        if(firstChar != null)
            content.append(firstChar);

        while(chars.hasMore()) {
            if( ! ALLOWED_WORD_BODY.test(String.valueOf(chars.peek()))) {
                // EOW
                break;
            }
            content.append(chars.next());
        }

        return content.toString();
    }

    private void addString() {
        StringBuilder sb = new StringBuilder();
        boolean escaped = false;
        boolean closed = false;
        while(chars.hasMore()) {
            char c = chars.next();
            if(escaped) {
                escaped = false;
                sb.append(c);
                continue;
            }
            if(c == '\\') {
                escaped = true;
                continue;
            }
            if(c == '"') {
                // EOS
                closed = true;
                break;
            }
            if(c == '\n') {
                // EOL ??
                throw new ParsingException(chars.pos(), "Unexpected carriage return in a string.");
            }

            // Char
            sb.append(c);
        }

        if(!closed)
            throw new ParsingException(chars.pos(), chars.peek(), "Cannot end file with a '\"' : expected a closing quote for the string.");

        tokens.add(Token.fromString(sb.toString(), chars.pos()));
    }

    private static boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private void addRaw(TokenType type) {
        tokens.add(type.toToken(chars.pos()));
    }


}
