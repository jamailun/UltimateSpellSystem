package fr.jamailun.ultimatespellsystem.dsl;

import fr.jamailun.ultimatespellsystem.dsl.nodes.StatementNode;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.*;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Central access to the DSL.
 */
public final class UltimateSpellSystemDSL {
    private UltimateSpellSystemDSL() {}

    /**
     * Parse a stream of tokens.
     * @param tokens the tokens to parse.
     * @return a parsed collection of statements.
     */
    public static @NotNull List<StatementNode> parse(@NotNull TokenStream tokens) {
        List<StatementNode> statements = new ArrayList<>();
        while(tokens.hasMore()) {
            if(tokens.peek().getType() == TokenType.EOF)
                break;
            StatementNode node = StatementNode.parseNextStatement(tokens);
            statements.add(node);
        }

        return statements;
    }

    /**
     * Parse a stream of characters.
     * @param chars the characters to parse.
     * @return a parsed collection of statements.
     */
    public static @NotNull List<StatementNode> parse(@NotNull CharStream chars) {
        TokenStream tokens = Tokenizer.tokenize(chars);
        return parse(tokens);
    }

    /**
     * Parse a string.
     * @param string the string to parse.
     * @return a parsed collection of statements.
     */
    public static @NotNull List<StatementNode> parse(@NotNull String string) {
        return parse(CharStream.from(string));
    }

    /**
     * Parse a file.
     * @param file the file to read and parse.
     * @return a parsed collection of statements.
     */
    public static @NotNull List<StatementNode> parse(@NotNull File file) {
        return parse(CharStream.from(file));
    }

}
