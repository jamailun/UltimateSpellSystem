package fr.jamailun.ultimatespellsystem.dsl.nodes.statements;

import fr.jamailun.ultimatespellsystem.dsl.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.StatementNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypesContext;
import fr.jamailun.ultimatespellsystem.dsl.visitor.StatementVisitor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

/**
 * Wraps an {@link ExpressionNode} as a {@link StatementNode}.
 */
@Getter
@RequiredArgsConstructor
public class SimpleExpressionStatement extends StatementNode {

    private final @NotNull ExpressionNode child;

    @Override
    public void visit(@NotNull StatementVisitor visitor) {
        visitor.handleSimpleExpression(this);
    }

    @Override
    public void validateTypes(@NotNull TypesContext context) {
        // Nothing to do.
    }

    @Override
    public String toString() {
        return "{" + child + "}";
    }
}
