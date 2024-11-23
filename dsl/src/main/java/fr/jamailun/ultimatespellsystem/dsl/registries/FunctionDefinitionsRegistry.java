package fr.jamailun.ultimatespellsystem.dsl.registries;

import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.functions.FunctionDefinition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * A registry for {@link FunctionDefinition}.
 */
public final class FunctionDefinitionsRegistry {
    private FunctionDefinitionsRegistry() {}

    private final static Map<String, FunctionDefinition> REGISTRY = new HashMap<>();

    /**
     * Register a new function definition.
     * @param functionDefinition the non-null function definition to register
     */
    public static void register(@NotNull FunctionDefinition functionDefinition) {
        REGISTRY.put(functionDefinition.id(), functionDefinition);
    }

    /**
     * Test if a function exist.
     * @param functionId the function ID to test.
     * @return true if a function with this ID has already been registered.
     * @see FunctionDefinition#id()
     */
    public static boolean exists(@NotNull String functionId) {
        return REGISTRY.containsKey(functionId);
    }

    /**
     * Find a function definition.
     * @param functionId the function ID to use.
     * @return {@code null} if no function with this ID has been registered.
     * @see FunctionDefinition#id()
     */
    public static @Nullable FunctionDefinition find(@NotNull String functionId) {
        return REGISTRY.get(functionId);
    }

}
