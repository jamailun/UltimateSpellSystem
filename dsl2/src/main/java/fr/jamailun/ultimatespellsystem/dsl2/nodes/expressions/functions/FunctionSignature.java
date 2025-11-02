package fr.jamailun.ultimatespellsystem.dsl2.nodes.expressions.functions;

import fr.jamailun.ultimatespellsystem.dsl2.nodes.type.Type;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Defines the unicity of a function.
 * @param name name of the function.
 * @param paramsTypes arguments types.
 */
public record FunctionSignature(
        @NotNull String name,
        @NotNull List<Type> paramsTypes
) {
    public static @NotNull FunctionSignature of(@NotNull String id, @NotNull List<FunctionArgument> args) {
        return new FunctionSignature(id, args.stream().map(FunctionArgument::type).toList());
    }
}
