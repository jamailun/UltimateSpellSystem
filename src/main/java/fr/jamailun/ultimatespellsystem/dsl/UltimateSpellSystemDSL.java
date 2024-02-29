package fr.jamailun.ultimatespellsystem.dsl;

import fr.jamailun.ultimatespellsystem.dsl.nodes.StatementNode;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UltimateSpellSystemDSL {

    public static List<StatementNode> parse(TokenStream tokens) {
        List<StatementNode> statements = new ArrayList<>();
        while(tokens.hasMore()) {
            if(tokens.peek().getType() == TokenType.EOF)
                break;
            StatementNode node = StatementNode.parseNextStatement(tokens);
            statements.add(node);
        }

        return statements;
    }

    public static List<StatementNode> parse(CharStream chars) {
        TokenStream tokens = Tokenizer.tokenize(chars);
        return parse(tokens);
    }

    public static List<StatementNode> parse(String string) {
        return parse(CharStream.from(string));
    }

    public static List<StatementNode> parse(File file) {
        return parse(CharStream.from(file));
    }

}
