package fr.jamailun.ultimatespellsystem.plugin.runner.nodes.expressions;

import fr.jamailun.ultimatespellsystem.api.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.api.runner.RuntimeStatement;
import fr.jamailun.ultimatespellsystem.api.runner.SpellRuntime;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

/**
 * Wrap an expression as a statement.
 */
@RequiredArgsConstructor
public class ExpressionWrapperNode extends RuntimeStatement {

    private final @NotNull RuntimeExpression expression;

    @Override
    public void run(@NotNull SpellRuntime runtime) {
        expression.evaluate(runtime);
    }
}
