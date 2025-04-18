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
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

/**
 * Classic while statement.
 */
@RequiredArgsConstructor
public class WhileLoopStatement extends StatementNode {

    private final ExpressionNode nodeCondition;
    @Getter private final StatementNode child;
    @Getter private final boolean whileFirst;

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

    /**
     * Parse a "while-loop" statement. Can be either a while/do or a do/while.
     * @param tokens streams of tokens.
     * @return a new instance.
     */
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

  @Override
    public String toString() {
        if(whileFirst)
            return "WHILE(" + nodeCondition + ") DO " + child;
        return "DO " + child + " WHILE(" + nodeCondition + ")";
    }
}
