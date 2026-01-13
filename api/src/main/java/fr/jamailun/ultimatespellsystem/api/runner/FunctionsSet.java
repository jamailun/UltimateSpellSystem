package fr.jamailun.ultimatespellsystem.api.runner;

import fr.jamailun.ultimatespellsystem.api.runner.functions.GlobalFunction;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.expressions.functions.FunctionSignature;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A set of functions.
 */
public interface FunctionsSet {

    /**
     * Get a function.
     * @param signature the function signature key.
     * @return the content of the variable, or {@code null} if not defined.
     */
    @Nullable GlobalFunction get(@NotNull FunctionSignature signature);

    /**
     * Create an inherited functions set.
     * @return a new instance. Mutating existing vars of the instance will mutate {@code this} instance.
     * But adding new functions will not.
     */
    @Contract(" -> new")
    @NotNull FunctionsSet inherit();

    /**
     * Register a new function.
     * @param function the function to register.
     */
    void register(@NotNull GlobalFunction function);

}
