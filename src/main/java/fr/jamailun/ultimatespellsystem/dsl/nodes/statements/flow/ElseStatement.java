package fr.jamailun.ultimatespellsystem.dsl.nodes.statements.flow;

import fr.jamailun.ultimatespellsystem.dsl.nodes.StatementNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypesContext;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.PreviousIndicator;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenStream;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenType;
import fr.jamailun.ultimatespellsystem.dsl.visitor.StatementVisitor;

public class ElseStatement extends StatementNode {

    private final StatementNode child;

    public ElseStatement(StatementNode child) {
        this.child = child;
    }

    @Override
    public void validateTypes(TypesContext context) {
        // nothing
    }

    public StatementNode getChild() {
        return child;
    }

    @Override
    public void visit(StatementVisitor visitor) {
        visitor.handleElse(this);
    }

    @PreviousIndicator(expected = TokenType.ELSE)
    public static ElseStatement parseElseStatement(TokenStream tokens) {
        // Content
        StatementNode child = StatementNode.parseNextStatement(tokens);

        // Return
        return new ElseStatement(child);
    }

    @Override
    public String toString() {
        return "ELSE : " + child;
    }
}
