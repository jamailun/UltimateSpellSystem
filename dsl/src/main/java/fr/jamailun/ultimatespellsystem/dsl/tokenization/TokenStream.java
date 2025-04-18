package fr.jamailun.ultimatespellsystem.dsl.tokenization;

import com.google.common.base.Preconditions;
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

    /**
     * Create a new instance from a tokens list.
     * @param tokens a non-null list of tokens.
     */
    public TokenStream(@NotNull List<Token> tokens) {
        this.tokens = tokens;
    }

    /**
     * Read the next character, don't move cursor.
     * @return a non-null token.
     * @throws IllegalStateException if no more data exist.
     */
    public @NotNull Token peek() {
        Preconditions.checkState(hasMore(), "No more data.");
        return tokens.get(index);
    }

    /**
     * Read the next character, then move cursor to the next token.
     * @return a non-null token.
     * @throws IllegalStateException if no more data exist.
     */
    public @NotNull Token next() {
        Preconditions.checkState(hasMore(), "No more data.");
        return tokens.get(index ++);
    }

    /**
     * Assert the next token is of a specific type, or throw.
     * @param type expected type.
     * @return a non-null token.
     * @throws IllegalStateException if no more data exist.
     * @throws SyntaxException if the next token does not match what was expected.
     */
    public @NotNull Token nextOrThrow(@NotNull TokenType type) {
        Token next = next();
        if(next.getType() != type)
            throw new SyntaxException(next, type);
        return next;
    }

    /**
     * Drop the current token, move the cursor.
     * @throws IllegalStateException if no more data exist.
     */
    public void drop() {
        Preconditions.checkState(hasMore(), "No more data.");
        index++;
    }

    /**
     * Assert the current token is of a specific type, then drop it.
     * @param expectedType expected type for the current token.
     * @throws IllegalStateException if no more data exist.
     * @throws SyntaxException if the next token does not match what was expected.
     */
    public void dropOrThrow(@NotNull TokenType expectedType) {
        Token next = next();
        if(next.getType() != expectedType)
            throw new SyntaxException(next, expectedType);
    }
    /**
     * Assert the current token is of a specific type, then drop it <b>if it matches</b>
     * @param types expected types for the current token.
     * @return true if the next token <b>does</b> match what was expected. Or if no more data exists.
     */
    public boolean dropOptional(@NotNull TokenType... types) {
        if(hasMore() && List.of(types).contains(peek().getType())) {
            drop();
            return true;
        }
        return false;
    }

    /**
     * Make the cursor go back.
     * @throws IllegalStateException if the cursor is not at the beginning.
     */
    public void back() {
        Preconditions.checkState(index > 0, "Index is at start, cannot go back.");
        index--;
    }

    /**
     * Test if more data exists.
     * @return true if more data can be read.
     */
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

    /**
     * Get the current token position.
     * @return the current position.
     * @throws IllegalStateException if no more data can be read.
     */
    public @NotNull TokenPosition position() {
        return peek().pos();
    }
}
