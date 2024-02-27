package fr.jamailun.ultimatespellsystem.dsl.nodes.statements.flow;

import fr.jamailun.ultimatespellsystem.dsl.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.StatementNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypesContext;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.PreviousIndicator;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenStream;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenType;
import fr.jamailun.ultimatespellsystem.dsl.visitor.StatementVisitor;

public class IfStatement extends StatementNode {

    private final ExpressionNode condition;
    private final StatementNode child;

    public IfStatement(ExpressionNode condition, StatementNode child) {
        this.condition = condition;
        this.child = child;
    }

    @Override
    public void validateTypes(TypesContext context) {
        assertExpressionType(condition, context, TypePrimitive.BOOLEAN);
    }

    public StatementNode getChild() {
        return child;
    }

    public ExpressionNode getCondition() {
        return condition;
    }

    @Override
    public void visit(StatementVisitor visitor) {
        visitor.handleIf(this);
    }

    @PreviousIndicator(expected = TokenType.IF)
    public static IfStatement parseIfStatement(TokenStream tokens) {
        // Condition
        tokens.dropOrThrow(TokenType.BRACKET_OPEN);
        ExpressionNode condition = ExpressionNode.readNextExpression(tokens);
        tokens.dropOrThrow(TokenType.BRACKET_CLOSE);

        // Content
        StatementNode child = StatementNode.parseNextStatement(tokens);

        // Return
        return new IfStatement(condition, child);
    }

    @Override
    public String toString() {
        return "IF(" + condition +") : " + child;
    }
}
