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

public class ForLoopStatement extends StatementNode {

    private final ExpressionNode nodeCondition;
    private final StatementNode initialization, iteration;
    private final StatementNode child;

    public ForLoopStatement(StatementNode initialization, ExpressionNode nodeCondition, StatementNode iteration, StatementNode child) {
        this.nodeCondition = nodeCondition;
        this.initialization = initialization;
        this.iteration = iteration;
        this.child = child;
    }

    @Override
    public void validateTypes(TypesContext context) {
        TypesContext childContext = context.childContext();
        initialization.validateTypes(childContext);
        assertExpressionType(nodeCondition, CollectionFilter.MONO_ELEMENT, childContext, TypePrimitive.BOOLEAN);
        iteration.validateTypes(childContext);
        child.validateTypes(childContext);
    }

    @Override
    public void visit(StatementVisitor visitor) {
        visitor.handleForLoop(this);
    }

    @PreviousIndicator(expected = TokenType.FOR)
    public static ForLoopStatement parseForLoop(TokenStream tokens) {
        tokens.dropOrThrow(TokenType.BRACKET_OPEN);

        StatementNode init = StatementNode.parseNextStatement(tokens);
        ExpressionNode condition = ExpressionNode.readNextExpression(tokens);
        tokens.dropOrThrow(TokenType.SEMI_COLON);
        StatementNode iterator = StatementNode.parseNextStatement(tokens);

        tokens.dropOrThrow(TokenType.BRACKET_CLOSE);
        StatementNode child = StatementNode.parseNextStatement(tokens);

        return new ForLoopStatement(init, condition, iterator, child);
    }

    public ExpressionNode getCondition() {
        return nodeCondition;
    }

    public StatementNode getInitialization() {
        return initialization;
    }

    public StatementNode getIteration() {
        return iteration;
    }

    public StatementNode getChild() {
        return child;
    }

    @Override
    public String toString() {
        return "FOR(" + initialization + "; " + nodeCondition + "; " + iteration + "): " + child;
    }
}
