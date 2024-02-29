package fr.jamailun.ultimatespellsystem.bukkit.runner.nodes.expressions;

import fr.jamailun.ultimatespellsystem.bukkit.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.bukkit.runner.SpellRuntime;

public class VariableNode extends RuntimeExpression {

    private final String variableName;


    public VariableNode(String varName) {
        this.variableName = varName;
    }

    @Override
    public Object evaluate(SpellRuntime runtime) {
        return runtime.variables().get(variableName);
    }

    @Override
    public String toString() {
        return "%" + variableName;
    }
}
