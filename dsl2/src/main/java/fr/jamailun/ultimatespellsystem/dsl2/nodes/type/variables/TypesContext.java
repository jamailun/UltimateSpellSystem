package fr.jamailun.ultimatespellsystem.dsl2.nodes.type.variables;

import fr.jamailun.ultimatespellsystem.dsl2.errors.SyntaxException;
import fr.jamailun.ultimatespellsystem.dsl2.library.ObjectsLibrary;
import fr.jamailun.ultimatespellsystem.dsl2.library.StructDefinition;
import fr.jamailun.ultimatespellsystem.dsl2.library.structs.ConsoleStruct;
import fr.jamailun.ultimatespellsystem.dsl2.library.structs.EntityStruct;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.statements.FunctionDeclarationStatement;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.type.Type;
import fr.jamailun.ultimatespellsystem.dsl2.tokenization.TokenPosition;
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
    private final ObjectsLibrary objectsLibrary;

    /**
     * Create a new context.<br/>
     * The variable {@code caster} will be registered.
     */
    public TypesContext() {
        this.objectsLibrary = new ObjectsLibrary(true);
        promiseVariable("caster", Type.of(EntityStruct.NAME));
        promiseVariable("console", Type.of(ConsoleStruct.NAME));
    }

    /**
     * Create a new context.
     * @param objectsLibrary reference library to use.
     */
    private TypesContext(@NotNull ObjectsLibrary objectsLibrary) {
        this.objectsLibrary = objectsLibrary;
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
        vars.get(varName).register(new VariableReference.Dynamic(variable));
        vars.get(varName).getType(this);
    }

    /**
     * Guarantee a variable will be set by a runtime implementation.
     * @param name the name of the variable.
     * @param type the type of the variable.
     */
    public void promiseVariable(@NotNull String name, @NotNull Type type) {
        vars.putIfAbsent(name, new VariableDefinition(name));
        vars.get(name).register(new VariableReference.Constant(type, TokenPosition.unknown()));
    }

    /**
     * Register a variable, with an already guaranteed type.
     * @param varName the variable name.
     * @param position the position of the declaration.
     * @param type the type of variable.
     */
    public void registerVariable(@NotNull TokenPosition position, @NotNull String varName, @NotNull Type type) {
        if("caster".equals(varName)) {
            throw new SyntaxException(position, "Cannot override variable '" + varName + "'.");
        }

        vars.putIfAbsent(varName, new VariableDefinition(varName));
        vars.get(varName).register(new VariableReference.Constant(type, position));
    }

    /**
     * Lookup for a specific variable.
     * @param varName the name of the variable to look for.
     * @return {@code null} if nothing was found.
     */
    public @Nullable VariableDefinition findVariable(@NotNull String varName) {
        return vars.get(varName);
    }

    public @Nullable Type findType(@NotNull String name) {
        return objectsLibrary.getType(name);
    }

    public @Nullable FunctionDeclarationStatement findFunction(@NotNull String name) {
        return objectsLibrary.getFunction(name);
    }

    public @Nullable StructDefinition findStruct(@NotNull String name) {
        return objectsLibrary.getStruct(name);
    }

    public @Nullable StructDefinition findStruct(@NotNull Type type) {
        if(type.isPrimitive()) {
            //TODO specialized structs ?
            return null;
        }
        return objectsLibrary.getStruct(type.getName());
    }

    /**
     * Register a new function to the context
     * @param declaration the declaration to use.
     */
    public void registerFunction(@NotNull FunctionDeclarationStatement declaration) {
        objectsLibrary.registerFunction(declaration);
    }

    /**
     * Create a new context. This allows sub-scopes to be independents.
     * @return a new instance of context, copying the current variables.
     */
    @Contract(" -> new")
    public @NotNull TypesContext childContext() {
        TypesContext child = new TypesContext(objectsLibrary);
        child.vars.putAll(vars);
        return child;
    }

}
