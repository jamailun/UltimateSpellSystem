package fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.functions;

import org.jetbrains.annotations.NotNull;

/**
 * A declaration of a function argument.
 * @param type the type of the argument.
 * @param debugName the name of the argument.
 * @param optional if true, argument is not mandatory.
 */
public record FunctionArgument(@NotNull FunctionType type, @NotNull String debugName, boolean optional) {
}
