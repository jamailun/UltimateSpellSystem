package fr.jamailun.ultimatespellsystem.dsl.nodes.type;

import fr.jamailun.ultimatespellsystem.dsl.errors.SyntaxException;
import fr.jamailun.ultimatespellsystem.dsl.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.VariableExpression;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

public class TypesContext {

    private static class VariableDefinition {
        final String name;
        final Type type;
        ExpressionNode expressionNode;

        VariableDefinition(String name, Type type, ExpressionNode node) {
            this.name = name;
            this.type = type;
            this.expressionNode = node;
        }
        VariableDefinition(String name, Type type) {
            this.name = name;
            this.type = type;
        }
    }

    private final Map<String, VariableDefinition> variables = new HashMap<>();

    public void registerAbsolute(String varName, Type type) {
        variables.put(varName, new VariableDefinition(varName, type));
    }

    public void registerVariable(String varName, Type type, ExpressionNode node) {
        if(variables.containsKey(varName)) {
            throw new SyntaxException(node.firstTokenPosition(), "Variable " + varName + " already defined.");
        }
        variables.put(varName, new VariableDefinition(varName, type, node));
    }

    public @Nullable Type findVariable(String varName) {
        if(variables.containsKey(varName))
            return variables.get(varName).type;
        return null;
    }

}
