package fr.jamailun.ultimatespellsystem.dsl.tokenization;

import fr.jamailun.ultimatespellsystem.dsl.errors.ParsingException;
import fr.jamailun.ultimatespellsystem.dsl.errors.SyntaxException;

import java.util.List;
import java.util.StringJoiner;

public class TokenStream {

    private final List<Token> tokens;
    private int index = 0;

    public TokenStream(List<Token> tokens) {
        this.tokens = tokens;
    }

    public Token peek() {
        if(!hasMore())
            throw new RuntimeException("No more data.");
        return tokens.get(index);
    }

    public Token next() {
        if(!hasMore())
            throw new RuntimeException("No more data.");
        return tokens.get(index ++);
    }

    public Token nextOrThrow(TokenType type) {
        Token next = next();
        if(next.getType() != type)
            throw new SyntaxException(next, type);
        return next;
    }

    public void drop() {
        if(!hasMore())
            throw new RuntimeException("No more data.");
        index++;
    }

    public void dropOrThrow(TokenType expectedType) {
        if(!hasMore())
            throw new RuntimeException("No more data.");
        Token next = next();
        if(next.getType() != expectedType)
            throw new SyntaxException(next, expectedType);
    }

    public void assertNextIs(TokenType... allowed) {
        if(!hasMore())
            throw new RuntimeException("No more data.");
        Token peek = peek();
        if(!List.of(allowed).contains(peek.getType()))
            throw new SyntaxException(peek, List.of(allowed));
    }

    public void dropOptional(TokenType type) {
        if(hasMore() && peek().getType() == type)
            drop();
    }

    public boolean hasMore() {
        return index < tokens.size();
    }

    @Override
    public String toString() {
        StringJoiner sj = new StringJoiner(", ");
        for(int i = index; i < tokens.size(); i++)
            sj.add(tokens.get(i).toString());
        return "TokenStream{index="+index+", TOKENS = [" + sj + "] }";
    }
}
