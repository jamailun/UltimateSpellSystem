package fr.jamailun.ultimatespellsystem.dsl.nodes.statements;

import fr.jamailun.ultimatespellsystem.dsl.nodes.StatementNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypesContext;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.PreviousIndicator;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenStream;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenType;
import fr.jamailun.ultimatespellsystem.dsl.visitor.StatementVisitor;

public class StopStatement extends StatementNode {

    @Override
    public void validateTypes(TypesContext context) {}

    @Override
    public void visit(StatementVisitor visitor) {
        visitor.handleStop(this);
    }

    @PreviousIndicator(expected = TokenType.STOP)
    public static StopStatement parseStop(TokenStream tokens) {
        tokens.dropOrThrow(TokenType.SEMI_COLON);
        return new StopStatement();
    }

    @Override
    public String toString() {
        return "STOP()";
    }
}
