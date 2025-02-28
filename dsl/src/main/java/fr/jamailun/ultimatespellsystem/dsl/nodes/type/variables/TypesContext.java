package fr.jamailun.ultimatespellsystem.dsl.nodes.type.variables;

import fr.jamailun.ultimatespellsystem.dsl.errors.SyntaxException;
import fr.jamailun.ultimatespellsystem.dsl.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Type;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenPosition;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * A context for type validation.
 */
public class TypesContext {

    private final Map<String, VariableDefinition> vars = new HashMap<>();

    /**
     * Create a new context, and register the {@code %caster} variable.
     */
    public TypesContext() {
        promiseVariable("caster", TypePrimitive.ENTITY.asType());
    }

    /**
     * Register a new variable.
     * @param varName the name of the variable.
     * @param variable the expression defining the variable.
     */
    public void registerVariable(@NotNull String varName, @NotNull ExpressionNode variable) {
        if("caster".equals(varName)) {
            throw new SyntaxException(variable.firstTokenPosition(), "Cannot override variable '%" + varName + "'.");
        }

        vars.putIfAbsent(varName, new VariableDefinition(varName));
        vars.get(varName).register(new VariableReference.Dynamic(varName, variable));
        vars.get(varName).getType(this);
    }

    /**
     * Guarantee a variable will be set by a runtime implementation.
     * @param name the name of the variable.
     * @param type the type of the variable.
     */
    public void promiseVariable(@NotNull String name, @NotNull Type type) {
        vars.putIfAbsent(name, new VariableDefinition(name));
        vars.get(name).register(new VariableReference.Constant(name, type, TokenPosition.unknown()));
    }

    /**
     * Register a variable, with an already guaranteed type.
     * @param varName the variable name.
     * @param position the position of the declaration.
     * @param type the type of variable.
     */
    public void registerVariable(@NotNull String varName, @NotNull TokenPosition position, @NotNull Type type) {
        if("caster".equals(varName)) {
            throw new SyntaxException(position, "Cannot override variable '%" + varName + "'.");
        }

        vars.putIfAbsent(varName, new VariableDefinition(varName));
        vars.get(varName).register(new VariableReference.Constant(varName, type, position));
    }

    /**
     * Lookup for a specific variable.
     * @param varName the name of the variable to look for.
     * @return {@code null} if nothing was found.
     */
    public @Nullable VariableDefinition findVariable(@NotNull String varName) {
        return vars.get(varName);
    }

    /**
     * Create a new context. This allows sub-scopes to be independents.
     * @return a new instance of context, copying the current variables.
     */
    @Contract(" -> new")
    public @NotNull TypesContext childContext() {
        TypesContext child = new TypesContext();
        child.vars.putAll(vars);
        return child;
    }

}
