package fr.jamailun.ultimatespellsystem.bukkit.runner.functions;

import fr.jamailun.ultimatespellsystem.api.bukkit.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.api.bukkit.runner.SpellRuntime;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.functions.FunctionArgument;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.functions.FunctionDefinition;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Type;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * A runnable code declaration
 */
@Getter
@RequiredArgsConstructor
public abstract class RunnableJavaFunction {

    private final @NotNull String id;
    private final @NotNull Type type;
    private final @NotNull List<FunctionArgument> arguments;

    /**
     * Compute the value, using the arguments.
     * @param arguments the arguments values returned. Must be computed in the runtime.
     * @param runtime the runtime of the spell.
     * @return the object returned by this expression.
     */
    public abstract Object compute(@NotNull List<RuntimeExpression> arguments, @NotNull SpellRuntime runtime);

    /**
     * Get the DSL definition.
     * @return a new, non-null Function Definition.
     */
    public final @NotNull FunctionDefinition getDslDefinition() {
        return new FunctionDefinition(id, type, arguments);
    }

}
