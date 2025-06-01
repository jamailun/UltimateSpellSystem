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
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * Statement of a {@code repeat <N> times}.
 */
public class RepeatStatement extends BlockHolder {

    /**
     * Name of the variable set by this statement.
     */
    public static final String INDEX_VARIABLE = "_repeat_index";

    private final @Nullable ExpressionNode delay; // optional
    @Getter private @Nullable final ExpressionNode totalCount; // either 'count' or 'totalDuration'
    @Getter private @NotNull final ExpressionNode period;
    @Getter private @Nullable final ExpressionNode totalDuration;

    public RepeatStatement(@NotNull StatementNode child, @Nullable ExpressionNode delay, @Nullable ExpressionNode totalCount, @NotNull ExpressionNode period, @Nullable ExpressionNode totalDuration) {
        super(child);
        this.delay = delay;
        this.totalCount = totalCount;
        this.period = period;
        this.totalDuration = totalDuration;
    }

    @Override
    public void validateTypes(@NotNull TypesContext context) {
        TypesContext childContext = context.childContext();
        childContext.promiseVariable(INDEX_VARIABLE, TypePrimitive.NUMBER.asType());

        if(delay != null)
            assertExpressionType(delay, CollectionFilter.MONO_ELEMENT, childContext, TypePrimitive.DURATION);
        if(totalCount != null)
            assertExpressionType(totalCount, CollectionFilter.MONO_ELEMENT, childContext, TypePrimitive.NUMBER);
        assertExpressionType(period, CollectionFilter.MONO_ELEMENT, childContext, TypePrimitive.DURATION);
        if(totalDuration != null)
            assertExpressionType(totalDuration, CollectionFilter.MONO_ELEMENT, childContext, TypePrimitive.DURATION);

        child.validateTypes(childContext.childContext());
    }

    public Optional<ExpressionNode> getDelay() {
        return Optional.ofNullable(delay);
    }

    /**
     * Parse a "repeat N times" statement.
     * @param tokens streams of tokens.
     * @return a new instance.
     */
    // REPEAT [[AFTER (DURATION)]] (COUNT) TIMES EVERY (FREQ): {}
    // REPEAT [[AFTER (DURATION)]] EVERY (FREQ) FOR (DURATION): {}
    @PreviousIndicator(expected = {TokenType.REPEAT})
    public static RepeatStatement parseRepeat(TokenStream tokens) {
        ExpressionNode delay = null;
        if(tokens.dropOptional(TokenType.AFTER)) {
            delay = ExpressionNode.readNextExpression(tokens);
        }

        ExpressionNode freq;
        ExpressionNode count;
        ExpressionNode duration;

        // 2nd syntax : EVERY f FOR d
        if(tokens.dropOptional(TokenType.EVERY)) {
            freq = ExpressionNode.readNextExpression(tokens);
            tokens.dropOrThrow(TokenType.FOR);
            duration = ExpressionNode.readNextExpression(tokens);
            count = null;
        }
        // 1st syntax : c TIMES EVERY f
        else {
            count = ExpressionNode.readNextExpression(tokens);
            tokens.dropOrThrow(TokenType.TIMES);
            tokens.dropOrThrow(TokenType.EVERY);
            freq = ExpressionNode.readNextExpression(tokens);
            duration = null;
        }

        // Always present: child nodes.
        tokens.dropOrThrow(TokenType.COLON);
        StatementNode child = StatementNode.parseNextStatement(tokens);
        tokens.dropOptional(TokenType.SEMI_COLON);
        return new RepeatStatement(child, delay, count, freq, duration);
    }

    @Override
    public void visit(@NotNull StatementVisitor visitor) {
        visitor.handleRepeatRun(this);
    }

    @Override
    public String toString() {
        return "RUN{"+(delay==null?"":" AFTER " + period)+" " + totalCount + " times every " + period + "}: " + child;
    }
}
