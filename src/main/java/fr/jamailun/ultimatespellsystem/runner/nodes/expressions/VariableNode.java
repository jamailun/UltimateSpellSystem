package fr.jamailun.ultimatespellsystem.runner.nodes.expressions;

import fr.jamailun.ultimatespellsystem.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.runner.SpellRuntime;

public class VariableNode extends RuntimeExpression {

    private final String variableName;


    public VariableNode(String varName) {
        this.variableName = varName;
    }

    @Override
    public Object evaluate(SpellRuntime runtime) {
        return runtime.variables().get(variableName);
    }
}
