package fr.jamailun.ultimatespellsystem.plugin.runner.nodes.functions;

import fr.jamailun.ultimatespellsystem.api.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.api.runner.RuntimeStatement;
import fr.jamailun.ultimatespellsystem.api.runner.SpellRuntime;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@RequiredArgsConstructor
public class StopNode extends RuntimeStatement {

    private final @Nullable RuntimeExpression exitCode;

    @Override
    public void run(@NotNull SpellRuntime runtime) {
        if(exitCode == null) {
            runtime.stop(0);
        } else {
            int code = runtime.safeEvaluate(exitCode, Double.class).intValue();
            runtime.stop(code);
        }
    }

    @Override
    public String toString() {
        return "STOP("+exitCode+")";
    }
}
