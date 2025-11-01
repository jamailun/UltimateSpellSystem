package fr.jamailun.ultimatespellsystem.plugin.runner.nodes.operators;

import fr.jamailun.ultimatespellsystem.api.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.api.runner.SpellRuntime;
import fr.jamailun.ultimatespellsystem.api.runner.errors.UnreachableRuntimeException;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class IncrementNode extends RuntimeExpression {

    private final @NotNull String varName;
    private final boolean increments;
    private final boolean postFix;

    @Override
    public Double evaluate(@NotNull SpellRuntime runtime) {
        Object value = runtime.variables().get(varName);
        if(!(value instanceof Number num))
            throw new UnreachableRuntimeException("Invalid type for variable " + varName + " : " + value);

        double input = num.doubleValue();
        double output = input + (increments ? 1 : -1);
        runtime.variables().set(varName, output);
        return postFix ? input : output;
    }
}
