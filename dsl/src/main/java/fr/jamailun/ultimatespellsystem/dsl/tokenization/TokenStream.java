package fr.jamailun.ultimatespellsystem.dsl.tokenization;

import fr.jamailun.ultimatespellsystem.dsl.errors.SyntaxException;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.StringJoiner;

/**
 * A stream of {@link Token}.
 */
public class TokenStream {

    private final List<Token> tokens;
    private int index = 0;

    public TokenStream(List<Token> tokens) {
        this.tokens = tokens;
    }

    public @NotNull Token peek() {
        if(!hasMore())
            throw new RuntimeException("No more data.");
        return tokens.get(index);
    }

    public @NotNull Token next() {
        if(!hasMore())
            throw new RuntimeException("No more data.");
        return tokens.get(index ++);
    }

    public @NotNull Token nextOrThrow(@NotNull TokenType type) {
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

    public void dropOrThrow(@NotNull TokenType expectedType) {
        if(!hasMore())
            throw new RuntimeException("No more data.");
        Token next = next();
        if(next.getType() != expectedType)
            throw new SyntaxException(next, expectedType);
    }

    public void assertNextIs(@NotNull TokenType... allowed) {
        if(!hasMore())
            throw new RuntimeException("No more data.");
        Token peek = peek();
        if(!List.of(allowed).contains(peek.getType()))
            throw new SyntaxException(peek, List.of(allowed));
    }

    public boolean dropOptional(@NotNull TokenType... types) {
        if(hasMore() && List.of(types).contains(peek().getType())) {
            drop();
            return true;
        }
        return false;
    }

    public void back() {
        if(index == 0)
            throw new RuntimeException("Index is 0, cannot go back.");
        index--;
    }

    public boolean hasMore() {
        return index < tokens.size();
    }

    @Override
    public @NotNull String toString() {
        StringJoiner sj = new StringJoiner(", ");
        for(int i = index; i < tokens.size(); i++)
            sj.add(tokens.get(i).toString());
        return "TokenStream{index="+index+", TOKENS = [" + sj + "] }";
    }

    public @NotNull TokenPosition position() {
        return peek().pos();
    }
}
