package fr.jamailun.ultimatespellsystem.api.runner.functions;

import fr.jamailun.ultimatespellsystem.api.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.api.runner.SpellRuntime;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.functions.FunctionArgument;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.functions.FunctionDefinition;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Type;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * A runnable code declaration
 */
@Getter
public abstract class RunnableJavaFunction {

    private final @NotNull String id;
    private final @NotNull Type type;
    private final @NotNull List<FunctionArgument> arguments;

    /**
     * New instance of a function.
     * @param id ID of the function. Alternate ids can be provided at registration-time.
     * @param type output type.
     * @param arguments arguments types.
     * @see FunctionArgument
     */
    public RunnableJavaFunction(@NotNull String id, @NotNull Type type, @NotNull List<FunctionArgument> arguments) {
        this.id = id;
        this.type = type;
        this.arguments = arguments;
    }

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
