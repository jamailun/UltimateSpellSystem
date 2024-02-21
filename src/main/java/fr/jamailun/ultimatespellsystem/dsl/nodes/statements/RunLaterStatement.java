package fr.jamailun.ultimatespellsystem.dsl.nodes.statements;

import fr.jamailun.ultimatespellsystem.dsl.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.StatementNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypesContext;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.PreviousIndicator;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenStream;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenType;
import fr.jamailun.ultimatespellsystem.dsl.visitor.StatementVisitor;

public class RunLaterStatement extends StatementNode {

    private final StatementNode statement;
    private final ExpressionNode duration;

    public RunLaterStatement(StatementNode statement, ExpressionNode duration) {
        this.statement = statement;
        this.duration = duration;
    }

    @Override
    public void validateTypes(TypesContext context) {
        assertExpressionType(duration, TypePrimitive.DURATION, context);
    }

    public StatementNode getStatement() {
        return statement;
    }

    public ExpressionNode getDuration() {
        return duration;
    }

    // RUN AFTER (DURATION): {}
    @PreviousIndicator(expected = {TokenType.RUN})
    public static RunLaterStatement parseRunLater(TokenStream tokens) {
        tokens.dropOrThrow(TokenType.AFTER);
        ExpressionNode duration = ExpressionNode.readNextExpression(tokens);
        tokens.dropOrThrow(TokenType.COLON);
        StatementNode child = StatementNode.parseNextStatement(tokens);
        tokens.dropOptional(TokenType.SEMI_COLON);
        return new RunLaterStatement(child, duration);
    }

    @Override
    public void visit(StatementVisitor visitor) {
        visitor.handleRunLater(this);
    }

    @Override
    public String toString() {
        return "RUN{AFTER " + duration + "}: " + statement;
    }
}
