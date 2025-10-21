package fr.jamailun.ultimatespellsystem.dsl2.nodes.statements;

import fr.jamailun.ultimatespellsystem.dsl2.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.StatementNode;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.type.CollectionFilter;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.type.variables.TypesContext;
import fr.jamailun.ultimatespellsystem.dsl2.tokenization.PreviousIndicator;
import fr.jamailun.ultimatespellsystem.dsl2.tokenization.TokenStream;
import fr.jamailun.ultimatespellsystem.dsl2.tokenization.TokenType;
import fr.jamailun.ultimatespellsystem.dsl2.visitor.StatementVisitor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A {@code stop} statement will stop the spell execution.
 */
@AllArgsConstructor
@Getter
public class ReturnStatement extends StatementNode {

    private final @Nullable ExpressionNode exitCodeNode;

    @Override
    public void validateTypes(@NotNull TypesContext context) {
        if(exitCodeNode != null)
            assertExpressionType(exitCodeNode, CollectionFilter.MONO_ELEMENT, context, TypePrimitive.NUMBER);
    }

    @Override
    public void visit(@NotNull StatementVisitor visitor) {
        visitor.handleReturn(this);
    }

    /**
     * Parse a STOP statement.
     * @param tokens streams of tokens.
     * @return a new instance.
     */
    @PreviousIndicator(expected = TokenType.RETURN)
    public static @NotNull ReturnStatement parseReturn(@NotNull TokenStream tokens) {
        if(tokens.dropOptional(TokenType.SEMI_COLON)) {
            return new ReturnStatement(null);
        } else {
            ExpressionNode exitNode = ExpressionNode.readNextExpression(tokens);
            tokens.dropOrThrow(TokenType.SEMI_COLON);
            return new ReturnStatement(exitNode);
        }
    }

    @Override
    public String toString() {
        return "RETURN" + (exitCodeNode==null?"":"(" + exitCodeNode + ")");
    }
}
