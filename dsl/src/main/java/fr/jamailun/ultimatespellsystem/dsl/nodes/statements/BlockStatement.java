package fr.jamailun.ultimatespellsystem.dsl.nodes.statements;

import fr.jamailun.ultimatespellsystem.dsl.nodes.StatementNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.variables.TypesContext;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.PreviousIndicator;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenStream;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenType;
import fr.jamailun.ultimatespellsystem.dsl.visitor.StatementVisitor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

/**
 * A block statement is a sequence of statements between brackets.
 */
@Getter
@RequiredArgsConstructor
public class BlockStatement extends StatementNode {

    protected final List<StatementNode> children;

    @Override
    public void validateTypes(@NotNull TypesContext context) {
        TypesContext contextChild = context.childContext();
        for(StatementNode child : children) {
            child.validateTypes(contextChild);
        }
    }

    @Override
    public void visit(@NotNull StatementVisitor visitor) {
        visitor.handleBlock(this);
    }

    /**
     * Parse a block statement.
     * @param tokens streams of tokens.
     * @return a new instance.
     */
    @PreviousIndicator(expected = {TokenType.BRACES_OPEN})
    public static @NotNull BlockStatement parseNextBlock(@NotNull TokenStream tokens) {
        List<StatementNode> list = new ArrayList<>();

        while(tokens.hasMore()) {
            if(tokens.peek().getType() == TokenType.BRACES_CLOSE) {
                break;
            }
            list.add(StatementNode.parseNextStatement(tokens));
        }
        tokens.dropOrThrow(TokenType.BRACES_CLOSE);

        return new BlockStatement(list);
    }

    @Override
    public String toString() {
        StringJoiner sj = new StringJoiner("; ", "{", "}");
        children.forEach(c -> sj.add(c.toString()));
        return sj.toString();
    }
}
