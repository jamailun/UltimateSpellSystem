package fr.jamailun.ultimatespellsystem.dsl.nodes.statements.blocks;

import fr.jamailun.ultimatespellsystem.dsl.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.StatementNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.CollectionFilter;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypesContext;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.PreviousIndicator;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenStream;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenType;
import fr.jamailun.ultimatespellsystem.dsl.visitor.StatementVisitor;

public class RunLaterStatement extends BlockHolder {

    private final ExpressionNode duration;

    public RunLaterStatement(StatementNode child, ExpressionNode duration) {
        super(child);
        this.duration = duration;
    }

    @Override
    public void validateTypes(TypesContext context) {
        assertExpressionType(duration, CollectionFilter.MONO_ELEMENT, context, TypePrimitive.DURATION);
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
        return "RUN{AFTER " + duration + "}: " + child;
    }
}
