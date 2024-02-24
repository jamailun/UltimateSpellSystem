package fr.jamailun.ultimatespellsystem.dsl.nodes.statements;

import fr.jamailun.ultimatespellsystem.dsl.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.StatementNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypesContext;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.PreviousIndicator;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenStream;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenType;
import fr.jamailun.ultimatespellsystem.dsl.visitor.StatementVisitor;

import java.util.Optional;

public class RepeatStatement extends StatementNode {

    private final StatementNode statement;
    private final ExpressionNode delay; // optional
    private final ExpressionNode count;
    private final ExpressionNode period;

    public RepeatStatement(StatementNode statement, ExpressionNode delay, ExpressionNode count, ExpressionNode period) {
        this.statement = statement;
        this.delay = delay;
        this.count = count;
        this.period = period;
    }

    @Override
    public void validateTypes(TypesContext context) {
        if(delay != null)
            assertExpressionType(delay, context, TypePrimitive.DURATION);
        assertExpressionType(count, context, TypePrimitive.NUMBER);
        assertExpressionType(period, context, TypePrimitive.DURATION);
    }

    public StatementNode getStatement() {
        return statement;
    }

    public ExpressionNode getPeriod() {
        return period;
    }

    public Optional<ExpressionNode> getDelay() {
        return Optional.ofNullable(delay);
    }

    public ExpressionNode getCount() {
        return count;
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
    public void visit(StatementVisitor visitor) {
        visitor.handleRepeatRun(this);
    }

    @Override
    public String toString() {
        return "RUN{"+(delay==null?"":" AFTER " + period)+" " + count + " times every " + period + "}: " + statement;
    }
}
