package fr.jamailun.ultimatespellsystem.plugin.runner.nodes.statements;

import fr.jamailun.ultimatespellsystem.api.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.api.runner.RuntimeStatement;
import fr.jamailun.ultimatespellsystem.api.runner.SpellRuntime;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A {@code return [expr]} node.
 */
@RequiredArgsConstructor
public class ReturnNode extends RuntimeStatement {

    private final @Nullable RuntimeExpression exitCode;

    @Override
    public void run(@NotNull SpellRuntime runtime) {
        Object output;
        if(exitCode == null) {
            output = null;
        } else {
            output = exitCode.evaluate(runtime);
        }
        runtime.statementReturn(output);
    }

    @Override
    public @NotNull String toString() {
        return "RETURN" + (exitCode == null ? "" : " " + exitCode);
    }
}
