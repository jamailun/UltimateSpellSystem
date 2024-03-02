package fr.jamailun.ultimatespellsystem.bukkit.runner.nodes.functions;

import fr.jamailun.ultimatespellsystem.bukkit.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Type;
import fr.jamailun.ultimatespellsystem.bukkit.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.bukkit.runner.RuntimeStatement;
import fr.jamailun.ultimatespellsystem.bukkit.runner.SpellRuntime;

import java.util.StringJoiner;

public class DefineNode extends RuntimeStatement {

    private final String varName;
    private final RuntimeExpression expression;
    private final Type type;

    public DefineNode(String varName, RuntimeExpression expression, Type type) {
        this.varName = varName;
        this.expression = expression;
        this.type = type;
    }

    @Override
    public void run(SpellRuntime runtime) {
        Class<?> clazz = type.primitive().clazz;
        if(clazz == null)
            throw new RuntimeException("Cannot run a null-typed expression : " + runtime);
        Object value = runtime.safeEvaluate(expression, clazz);
        UltimateSpellSystem.logDebug("Define %"+varName + " with == (" + value + ")");
        runtime.variables().set(varName, value);
    }

    @Override
    public String toString() {
        return "DEFINE %" + varName + " = " + expression;
    }
}