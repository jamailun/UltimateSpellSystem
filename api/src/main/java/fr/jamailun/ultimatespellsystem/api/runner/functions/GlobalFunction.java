package fr.jamailun.ultimatespellsystem.api.runner.functions;

import fr.jamailun.ultimatespellsystem.api.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.api.runner.SpellRuntime;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.expressions.functions.FunctionSignature;
import fr.jamailun.ultimatespellsystem.dsl2.tokenization.TokenPosition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * A global function. Either from the Java code, or from the USS script.
 */
public interface GlobalFunction {

    /**
     * Get the signature of the function.
     * @return the instance function signature.
     */
    @NotNull FunctionSignature getSignature();

    /**
     * Call the function with parameters.
     * @param pos token position.
     * @param arguments params of the function.
     * @param runtime spell runtime.
     * @return the output value.
     */
    @Nullable Object call(@NotNull TokenPosition pos, @NotNull List<RuntimeExpression> arguments, @NotNull SpellRuntime runtime);

}
