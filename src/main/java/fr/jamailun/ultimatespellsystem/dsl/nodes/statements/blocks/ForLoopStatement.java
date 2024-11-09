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
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@Getter
@RequiredArgsConstructor
public class ForLoopStatement extends StatementNode {

    private final StatementNode initialization;
    private final ExpressionNode condition;
    private final StatementNode iteration;
    private final StatementNode child;

    @Override
    public void validateTypes(@NotNull TypesContext context) {
        TypesContext childContext = context.childContext();
        initialization.validateTypes(childContext);
        assertExpressionType(condition, CollectionFilter.MONO_ELEMENT, childContext, TypePrimitive.BOOLEAN);
        iteration.validateTypes(childContext);
        child.validateTypes(childContext);
    }

    @Override
    public void visit(@NotNull StatementVisitor visitor) {
        visitor.handleForLoop(this);
    }

    @PreviousIndicator(expected = TokenType.FOR)
    public static @NotNull ForLoopStatement parseForLoop(@NotNull TokenStream tokens) {
        tokens.dropOrThrow(TokenType.BRACKET_OPEN);

        StatementNode init = StatementNode.parseNextStatement(tokens);
        ExpressionNode condition = ExpressionNode.readNextExpression(tokens);
        tokens.dropOrThrow(TokenType.SEMI_COLON);
        StatementNode iterator = StatementNode.parseNextStatement(tokens);

        tokens.dropOrThrow(TokenType.BRACKET_CLOSE);
        StatementNode child = StatementNode.parseNextStatement(tokens);

        return new ForLoopStatement(init, condition, iterator, child);
    }

    @Override
    public String toString() {
        return "FOR(" + initialization + "; " + condition + "; " + iteration + "): " + child;
    }
}
