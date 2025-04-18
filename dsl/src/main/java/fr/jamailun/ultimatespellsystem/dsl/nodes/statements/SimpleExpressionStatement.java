package fr.jamailun.ultimatespellsystem.dsl.nodes.statements;

import fr.jamailun.ultimatespellsystem.dsl.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.StatementNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.variables.TypesContext;
import fr.jamailun.ultimatespellsystem.dsl.visitor.StatementVisitor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

/**
 * Wraps an {@link ExpressionNode} as a {@link StatementNode}.
 */
@Getter
public class SimpleExpressionStatement extends StatementNode {

    private final @NotNull ExpressionNode child;

    /**
     * Create a new instance.
     * @param child wrapped expression, as a statement.
     */
    public SimpleExpressionStatement(@NotNull ExpressionNode child) {
        this.child = child;
    }

    @Override
    public void visit(@NotNull StatementVisitor visitor) {
        visitor.handleSimpleExpression(this);
    }

    @Override
    public void validateTypes(@NotNull TypesContext context) {
        child.validateTypes(context);
    }

    @Override
    public String toString() {
        return "{" + child + "}";
    }
}
