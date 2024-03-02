package fr.jamailun.ultimatespellsystem.dsl.nodes.type;

import fr.jamailun.ultimatespellsystem.dsl.errors.SyntaxException;
import fr.jamailun.ultimatespellsystem.dsl.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenPosition;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * A context for type validation.
 */
public class TypesContext {

    /**
     * A definition of a variable.
     */
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

    private final Map<String, VariableDefinition> variables = new HashMap<>();

    /**
     * Create a new context, and register the '%caster' variable.
     */
    public TypesContext() {
        registerAbsolute("caster", TypePrimitive.ENTITY.asType());
    }

    public void registerAbsolute(String varName, Type type) {
        variables.put(varName, new VariableDefinition(type));
    }

    /**
     * Register a new variable.
     * @param varName the name of the variable.
     * @param variable the expression defining the variable.
     */
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

    /**
     * Lookup for a specific variable.
     * @param varName the name of the variable to look for.
     * @return null if nothing was found.
     */
    public @Nullable VariableDefinition findVariable(String varName) {
        if(variables.containsKey(varName))
            return variables.get(varName);
        return null;
    }

    /**
     * Create a new context. This allows sub-scopes to be independents.
     * @return a new instance of context, copying the current variables.
     */
    public TypesContext childContext() {
        TypesContext child = new TypesContext();
        child.variables.putAll(variables);
        return child;
    }

}
