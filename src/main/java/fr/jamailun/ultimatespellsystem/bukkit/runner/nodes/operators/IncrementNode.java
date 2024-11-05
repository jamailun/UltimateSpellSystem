package fr.jamailun.ultimatespellsystem.bukkit.runner.nodes.operators;

import fr.jamailun.ultimatespellsystem.bukkit.runner.RuntimeStatement;
import fr.jamailun.ultimatespellsystem.bukkit.runner.SpellRuntime;
import fr.jamailun.ultimatespellsystem.bukkit.runner.errors.UnreachableRuntimeException;
import org.jetbrains.annotations.NotNull;

public class IncrementNode extends RuntimeStatement {

    private final String varName;
    private final boolean increments;

    public IncrementNode(@NotNull String varName, boolean increments) {
        this.varName = varName;
        this.increments = increments;
    }

    @Override
    public void run(@NotNull SpellRuntime runtime) {
        Object value = runtime.variables().get(varName);
        if(value instanceof Double d) {
            double v = d + (increments ? 1 : -1);
            runtime.variables().set(varName, v);
            return;
        }
        throw new UnreachableRuntimeException("Invalid type for variable " + varName + " : " + value);
    }

}
