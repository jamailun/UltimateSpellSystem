package fr.jamailun.ultimatespellsystem.dsl;

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

    public void drop() {
        if(!hasMore())
            throw new RuntimeException("No more data.");
        index++;
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
