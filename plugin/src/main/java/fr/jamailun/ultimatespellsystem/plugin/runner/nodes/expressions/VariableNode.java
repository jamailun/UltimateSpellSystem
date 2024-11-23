package fr.jamailun.ultimatespellsystem.plugin.runner.nodes.expressions;

import fr.jamailun.ultimatespellsystem.api.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.api.runner.SpellRuntime;
import org.jetbrains.annotations.NotNull;

public class VariableNode extends RuntimeExpression {

    private final String variableName;


    public VariableNode(String varName) {
        this.variableName = varName;
    }

    @Override
    public Object evaluate(@NotNull SpellRuntime runtime) {
        return runtime.variables().get(variableName);
    }

    @Override
    public String toString() {
        return "%" + variableName;
    }
}
