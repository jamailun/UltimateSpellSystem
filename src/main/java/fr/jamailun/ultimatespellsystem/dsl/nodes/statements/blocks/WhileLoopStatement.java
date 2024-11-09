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
import org.jetbrains.annotations.NotNull;

public class WhileLoopStatement extends StatementNode {

    private final ExpressionNode nodeCondition;
    private final StatementNode child;
    private final boolean whileFirst;

    public WhileLoopStatement(ExpressionNode nodeCondition, StatementNode child, boolean whileFirst) {
        this.nodeCondition = nodeCondition;
        this.child = child;
        this.whileFirst = whileFirst;
    }

    @Override
    public void validateTypes(@NotNull TypesContext context) {
        TypesContext childContext = context.childContext();

        assertExpressionType(nodeCondition, CollectionFilter.MONO_ELEMENT, childContext, TypePrimitive.BOOLEAN);
        child.validateTypes(childContext);
    }

    @Override
    public void visit(@NotNull StatementVisitor visitor) {
        visitor.handleWhileLoop(this);
    }

    // WHILE(<CDT>) <BLOCK>
    // DO <BLOCK> WHILE(<CDT>)
    @PreviousIndicator(expected = {TokenType.WHILE, TokenType.DO})
    public static WhileLoopStatement parseWhileLoop(TokenStream tokens, boolean wasWhile) {
        ExpressionNode condition = null;

        if(wasWhile) {
            tokens.dropOrThrow(TokenType.BRACKET_OPEN);
            condition = ExpressionNode.readNextExpression(tokens);
            tokens.dropOrThrow(TokenType.BRACKET_CLOSE);
        }

        StatementNode child = StatementNode.parseNextStatement(tokens);

        if( ! wasWhile) {
            tokens.dropOrThrow(TokenType.WHILE);
            tokens.dropOrThrow(TokenType.BRACKET_OPEN);
            condition = ExpressionNode.readNextExpression(tokens);
            tokens.dropOrThrow(TokenType.BRACKET_CLOSE);
            tokens.dropOptional(TokenType.SEMI_COLON);
        }

        return new WhileLoopStatement(condition, child, wasWhile);
    }

    public ExpressionNode getCondition() {
        return nodeCondition;
    }

    public StatementNode getChild() {
        return child;
    }

    public boolean isWhileFirst() {
        return whileFirst;
    }

    @Override
    public String toString() {
        if(whileFirst)
            return "WHILE(" + nodeCondition + ") DO " + child;
        return "DO " + child + " WHILE(" + nodeCondition + ")";
    }
}
