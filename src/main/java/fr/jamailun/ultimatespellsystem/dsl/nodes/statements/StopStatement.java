package fr.jamailun.ultimatespellsystem.dsl.nodes.statements;

import fr.jamailun.ultimatespellsystem.dsl.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.StatementNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.CollectionFilter;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypesContext;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.PreviousIndicator;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenStream;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenType;
import fr.jamailun.ultimatespellsystem.dsl.visitor.StatementVisitor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@AllArgsConstructor
@Getter
public class StopStatement extends StatementNode {

    private final @Nullable ExpressionNode exitCodeNode;

    @Override
    public void validateTypes(@NotNull TypesContext context) {
        if(exitCodeNode != null)
            assertExpressionType(exitCodeNode, CollectionFilter.MONO_ELEMENT, context, TypePrimitive.NUMBER);
    }

    @Override
    public void visit(@NotNull StatementVisitor visitor) {
        visitor.handleStop(this);
    }

    @PreviousIndicator(expected = TokenType.STOP)
    public static @NotNull StopStatement parseStop(@NotNull TokenStream tokens) {
        if(tokens.dropOptional(TokenType.SEMI_COLON)) {
            return new StopStatement(null);
        } else {
            ExpressionNode exitNode = ExpressionNode.readNextExpression(tokens);
            tokens.dropOrThrow(TokenType.SEMI_COLON);
            return new StopStatement(exitNode);
        }
    }

    @Override
    public String toString() {
        return "STOP" + (exitCodeNode==null?"":"(" + exitCodeNode + ")");
    }
}
