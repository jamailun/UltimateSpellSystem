package fr.jamailun.ultimatespellsystem.dsl2.nodes.expressions.functions;

import fr.jamailun.ultimatespellsystem.dsl2.nodes.type.Type;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.type.TypePrimitive;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * A declaration of a function argument.
 * @param type the type of the argument.
 * @param debugName the name of the argument.
 * @param optional if true, argument is not mandatory.
 */
public record FunctionArgument(@NotNull Type type, @NotNull String debugName, boolean optional) {
    @Contract(pure = true)
    @Override
    public @NotNull String toString() {
        return type + " " + debugName + (optional?"*":"");
    }

    public static FunctionArgument of(@NotNull String type) {
        return new FunctionArgument(Type.of(type), type, false);
    }

    public static FunctionArgument of(@NotNull TypePrimitive type) {
        return new FunctionArgument(Type.of(type), type.name().toLowerCase(), false);
    }
}
