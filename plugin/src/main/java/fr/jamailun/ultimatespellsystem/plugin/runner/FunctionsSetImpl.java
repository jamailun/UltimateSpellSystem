package fr.jamailun.ultimatespellsystem.plugin.runner;

import fr.jamailun.ultimatespellsystem.api.runner.FunctionsSet;
import fr.jamailun.ultimatespellsystem.api.runner.functions.GlobalFunction;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.expressions.functions.FunctionSignature;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * A set of keyed variables.
 */
public final class FunctionsSetImpl implements FunctionsSet {

    private final Map<FunctionSignature, GlobalFunction> functions = new HashMap<>();

    @Override
    public @Nullable GlobalFunction get(@NotNull FunctionSignature signature) {
        return functions.get(signature);
    }

    @Override
    public @NotNull FunctionsSetImpl inherit() {
        FunctionsSetImpl output = new FunctionsSetImpl();
        output.functions.putAll(functions);
        return output;
    }

    @Override
    public void register(@NotNull GlobalFunction function) {
        functions.put(function.getSignature(), function);
    }
}
