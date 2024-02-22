package fr.jamailun.ultimatespellsystem.runner.nodes;

import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Type;
import fr.jamailun.ultimatespellsystem.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.runner.SpellRuntime;

public class VariableNode extends RuntimeExpression {

    private final String variableName;
    private final Type type;


    public VariableNode(String varName, Type type) {
        this.variableName = varName;
        this.type = type;
    }

    @Override
    public Object evaluate(SpellRuntime runtime) {
        Class<?> clazz = type.primitive().clazz;
        if(clazz == null)
            throw new RuntimeException("Cannot accept NULL-typed primitives : " + type);
        return runtime.variables().get(variableName, clazz);
    }
}
