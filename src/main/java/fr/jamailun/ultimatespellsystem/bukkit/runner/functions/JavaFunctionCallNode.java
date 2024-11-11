package fr.jamailun.ultimatespellsystem.bukkit.runner.functions;

import fr.jamailun.ultimatespellsystem.api.bukkit.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.api.bukkit.runner.SpellRuntime;
import fr.jamailun.ultimatespellsystem.api.bukkit.runner.functions.RunnableJavaFunction;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * A Node that call some java.
 */
@RequiredArgsConstructor
public class JavaFunctionCallNode extends RuntimeExpression {

    private final RunnableJavaFunction function;
    private final List<RuntimeExpression> arguments;

    @Override
    public Object evaluate(@NotNull SpellRuntime runtime) {
        return function.compute(arguments, runtime);
    }
}
