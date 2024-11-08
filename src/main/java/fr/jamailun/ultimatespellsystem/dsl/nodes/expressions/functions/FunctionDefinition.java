package fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.functions;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Definition for a function to run. But because this is the DSL only, we don't care about really handling the functions.
 * @param id the ID of the function.
 * @param returnedType the type returned by this function.
 * @param arguments the arguments.
 */
public record FunctionDefinition(@NotNull String id, @NotNull FunctionType returnedType, @NotNull List<FunctionArgument> arguments) {

    public int mandatoryArgumentsCount() {
        return (int) arguments.stream()
                .filter(a -> ! a.optional())
                .count();

    }

}
