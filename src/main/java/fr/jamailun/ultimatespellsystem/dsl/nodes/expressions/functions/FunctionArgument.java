package fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.functions;

import org.jetbrains.annotations.NotNull;

/**
 * A declaration of a function argument.
 * @param type the type of the argument.
 * @param debugName the name fo the argument.
 */
public record FunctionArgument(@NotNull FunctionType type, @NotNull String debugName, boolean optional) {
}
