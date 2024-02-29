package fr.jamailun.ultimatespellsystem.dsl.nodes.statements.blocks;

import fr.jamailun.ultimatespellsystem.dsl.nodes.StatementNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypesContext;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.PreviousIndicator;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenStream;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenType;
import fr.jamailun.ultimatespellsystem.dsl.visitor.StatementVisitor;

public class ElseStatement extends BlockHolder {

    public ElseStatement(StatementNode child) {
        super(child);
    }

    @Override
    public void validateTypes(TypesContext context) {
        // nothing
    }

    @Override
    public void visit(StatementVisitor visitor) {
        // NOP !!
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
