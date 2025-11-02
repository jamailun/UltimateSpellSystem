package fr.jamailun.ultimatespellsystem.plugin.runner.nodes.operators.objects;

import fr.jamailun.ultimatespellsystem.api.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.api.runner.SpellRuntime;
import fr.jamailun.ultimatespellsystem.api.runner.functions.GlobalFunction;
import fr.jamailun.ultimatespellsystem.dsl2.errors.UnknownFunctionException;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.expressions.functions.FunctionSignature;
import fr.jamailun.ultimatespellsystem.dsl2.tokenization.TokenPosition;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Getter on a field of an object.
 */
@RequiredArgsConstructor
public class GlobalFunctionCallNode extends RuntimeExpression {

    private final TokenPosition pos;
    private final FunctionSignature signature;
    private final List<RuntimeExpression> parameters;

    @Override
    public @Nullable Object evaluate(@NotNull SpellRuntime runtime) {
        // Find function
        GlobalFunction function = runtime.functions().get(signature);
        if(function == null)
            throw new UnknownFunctionException(pos, signature.name());

        // Call function
        return function.call(pos, parameters, runtime);
    }

    @Override
    public @NotNull String toString() {
        return signature.name() + "(" + parameters + ")";
    }
}
