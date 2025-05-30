package fr.jamailun.ultimatespellsystem.dsl.nodes.statements.blocks;

import fr.jamailun.ultimatespellsystem.dsl.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.StatementNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.CollectionFilter;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.variables.TypesContext;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.PreviousIndicator;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenStream;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenType;
import fr.jamailun.ultimatespellsystem.dsl.visitor.StatementVisitor;
import org.jetbrains.annotations.NotNull;

/**
 * Run a block of code after a delay.
 */
public class RunLaterStatement extends BlockHolder {

    private final ExpressionNode duration;

    public RunLaterStatement(@NotNull StatementNode child, @NotNull ExpressionNode duration) {
        super(child);
        this.duration = duration;
    }

    @Override
    public void validateTypes(@NotNull TypesContext context) {
        assertExpressionType(duration, CollectionFilter.MONO_ELEMENT, context, TypePrimitive.DURATION);
        child.validateTypes(context.childContext());
    }

    public @NotNull ExpressionNode getDuration() {
        return duration;
    }

    /**
     * Parse a "run later" statement.
     * @param tokens streams of tokens.
     * @return a new instance.
     */
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
    public void visit(@NotNull StatementVisitor visitor) {
        visitor.handleRunLater(this);
    }

    @Override
    public String toString() {
        return "RUN{AFTER " + duration + "}: " + child;
    }
}
