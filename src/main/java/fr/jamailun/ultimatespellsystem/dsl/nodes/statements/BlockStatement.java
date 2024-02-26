package fr.jamailun.ultimatespellsystem.dsl.nodes.statements;

import fr.jamailun.ultimatespellsystem.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.dsl.nodes.StatementNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypesContext;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.PreviousIndicator;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenStream;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenType;
import fr.jamailun.ultimatespellsystem.dsl.visitor.StatementVisitor;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class BlockStatement extends StatementNode {

    protected final List<StatementNode> children;

    public BlockStatement(List<StatementNode> children) {
        this.children = children;
    }

    @Override
    public void validateTypes(TypesContext context) {
        TypesContext contextChild = context.childContext();
        for(StatementNode child : children) {
            child.validateTypes(contextChild);
        }
    }

    @Override
    public void visit(StatementVisitor visitor) {
        visitor.handleBlock(this);
    }

    public List<StatementNode> getChildren() {
        return children;
    }

    @PreviousIndicator(expected = {TokenType.BRACES_OPEN})
    public static BlockStatement parseNextBlock(TokenStream tokens) {
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
