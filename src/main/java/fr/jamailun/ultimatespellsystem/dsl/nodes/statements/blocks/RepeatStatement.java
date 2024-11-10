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
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class RepeatStatement extends BlockHolder {

    private final ExpressionNode delay; // optional
    @Getter private @NotNull final ExpressionNode count;
    @Getter private @NotNull final ExpressionNode period;

    public RepeatStatement(StatementNode child, ExpressionNode delay, @NotNull ExpressionNode count, @NotNull ExpressionNode period) {
        super(child);
        this.delay = delay;
        this.count = count;
        this.period = period;
    }

    @Override
    public void validateTypes(@NotNull TypesContext context) {
        if(delay != null)
            assertExpressionType(delay, CollectionFilter.MONO_ELEMENT, context, TypePrimitive.DURATION);
        assertExpressionType(count, CollectionFilter.MONO_ELEMENT, context, TypePrimitive.NUMBER);
        assertExpressionType(period, CollectionFilter.MONO_ELEMENT, context, TypePrimitive.DURATION);
    }

    public Optional<ExpressionNode> getDelay() {
        return Optional.ofNullable(delay);
    }

    // REPEAT [[AFTER (DURATION)]] (COUNT) TIMES EVERY (DURATION): {}
    @PreviousIndicator(expected = {TokenType.REPEAT})
    public static RepeatStatement parseRepeat(TokenStream tokens) {
        ExpressionNode delay = null;
        if(tokens.dropOptional(TokenType.AFTER)) {
            delay = ExpressionNode.readNextExpression(tokens);
        }
        ExpressionNode count = ExpressionNode.readNextExpression(tokens);
        tokens.dropOrThrow(TokenType.TIMES);
        tokens.dropOrThrow(TokenType.EVERY);
        ExpressionNode duration = ExpressionNode.readNextExpression(tokens);
        tokens.dropOrThrow(TokenType.COLON);
        StatementNode child = StatementNode.parseNextStatement(tokens);
        tokens.dropOptional(TokenType.SEMI_COLON);
        return new RepeatStatement(child, delay, count, duration);
    }

    @Override
    public void visit(@NotNull StatementVisitor visitor) {
        visitor.handleRepeatRun(this);
    }

    @Override
    public String toString() {
        return "RUN{"+(delay==null?"":" AFTER " + period)+" " + count + " times every " + period + "}: " + child;
    }
}
