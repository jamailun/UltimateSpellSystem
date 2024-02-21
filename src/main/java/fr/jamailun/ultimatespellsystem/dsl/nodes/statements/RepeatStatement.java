package fr.jamailun.ultimatespellsystem.dsl.nodes.statements;

import fr.jamailun.ultimatespellsystem.dsl.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.StatementNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypesContext;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.PreviousIndicator;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenStream;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenType;
import fr.jamailun.ultimatespellsystem.dsl.visitor.StatementVisitor;

public class RepeatStatement extends StatementNode {

    private final StatementNode statement;
    private final ExpressionNode delay; // optional
    private final ExpressionNode count;
    private final ExpressionNode duration;

    public RepeatStatement(StatementNode statement, ExpressionNode delay, ExpressionNode count, ExpressionNode duration) {
        this.statement = statement;
        this.delay = delay;
        this.count = count;
        this.duration = duration;
    }

    @Override
    public void validateTypes(TypesContext context) {
        if(delay != null)
            assertExpressionType(delay, TypePrimitive.DURATION, context);
        assertExpressionType(count, TypePrimitive.NUMBER, context);
        assertExpressionType(duration, TypePrimitive.DURATION, context);
    }

    public StatementNode getStatement() {
        return statement;
    }

    public ExpressionNode getDuration() {
        return duration;
    }

    public ExpressionNode getDelay() {
        return delay;
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
        return "RUN{"+(delay==null?"":" AFTER " + duration)+" " + count + " times every " + duration + "}: " + statement;
    }
}
