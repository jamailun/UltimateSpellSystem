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
import org.jetbrains.annotations.Nullable;

/**
 * A for loop.
 */
@Getter
@RequiredArgsConstructor
public class ForLoopStatement extends StatementNode {

    private final @Nullable StatementNode initialization;
    private final ExpressionNode condition;
    private final @Nullable StatementNode iteration;
    private final StatementNode child;

    @Override
    public void validateTypes(@NotNull TypesContext context) {
        TypesContext childContext = context.childContext();
        if(initialization != null) initialization.validateTypes(childContext);
        assertExpressionType(condition, CollectionFilter.MONO_ELEMENT, childContext, TypePrimitive.BOOLEAN);
        if(iteration != null) iteration.validateTypes(childContext);
        child.validateTypes(childContext);
    }

    @Override
    public void visit(@NotNull StatementVisitor visitor) {
        visitor.handleForLoop(this);
    }

    /**
     * Parse a for-loop statement.
     * @param tokens streams of tokens.
     * @return a new instance.
     */
    @PreviousIndicator(expected = TokenType.FOR)
    public static @NotNull ForLoopStatement parseForLoop(@NotNull TokenStream tokens) {
        tokens.dropOrThrow(TokenType.BRACKET_OPEN);

        // Optional init
        StatementNode init;
        if(tokens.peek().getType() == TokenType.SEMI_COLON) {
            init = null;
            tokens.drop();
        } else {
            init = StatementNode.parseNextStatement(tokens);
        }

        // Required condition
        ExpressionNode condition = ExpressionNode.readNextExpression(tokens);
        tokens.dropOrThrow(TokenType.SEMI_COLON);

        // Optional iteration
        StatementNode iterator;
        if(tokens.dropOptional(TokenType.BRACKET_CLOSE)) {
            iterator = null;
        } else {
            iterator = StatementNode.parseNextStatement(tokens);
            tokens.dropOrThrow(TokenType.BRACKET_CLOSE);
        }
        StatementNode child = StatementNode.parseNextStatement(tokens);

        return new ForLoopStatement(init, condition, iterator, child);
    }

    @Override
    public String toString() {
        return "FOR(" + initialization + "; " + condition + "; " + iteration + "): " + child;
    }
}
