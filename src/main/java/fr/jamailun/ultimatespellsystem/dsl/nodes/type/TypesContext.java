package fr.jamailun.ultimatespellsystem.dsl.nodes.type;

import fr.jamailun.ultimatespellsystem.dsl.errors.SyntaxException;
import fr.jamailun.ultimatespellsystem.dsl.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.VariableExpression;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.Token;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenPosition;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class TypesContext {

    public static class VariableDefinition {
        Type type;
        ExpressionNode nodeReference;

        VariableDefinition(Type type) {
            this.type = type;
        }
        VariableDefinition(ExpressionNode nodeReference) {
            this.nodeReference = nodeReference;
        }

        public Type computeType(TypesContext context) {
            if(type != null)
                return type;
            nodeReference.validateTypes(context);
            return nodeReference.getExpressionType();
        }

    }

    public TypesContext() {
        registerAbsolute("caster", TypePrimitive.ENTITY.asType());
    }

    private final Map<String, VariableDefinition> variables = new HashMap<>();

    public void registerAbsolute(String varName, Type type) {
        variables.put(varName, new VariableDefinition(type));
    }

    public void registerVariable(String varName, ExpressionNode variable) {
        if(variables.containsKey(varName)) {
            throw new SyntaxException(variable.firstTokenPosition(), "Variable " + varName + " already defined.");
        }
        variables.put(varName, new VariableDefinition(variable));
    }

    public void registerVariable(String varName, TokenPosition position, Type type) {
        if(variables.containsKey(varName)) {
            throw new SyntaxException(position, "Variable " + varName + " already defined.");
        }
        variables.put(varName, new VariableDefinition(type));
    }

    public @Nullable VariableDefinition findVariable(String varName) {
        if(variables.containsKey(varName))
            return variables.get(varName);
        return null;
    }

    public TypesContext childContext() {
        TypesContext child = new TypesContext();
        child.variables.putAll(variables);
        return child;
    }

}
