package fr.jamailun.ultimatespellsystem.bukkit.runner.functions;

import fr.jamailun.ultimatespellsystem.bukkit.runner.SpellRuntime;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.functions.FunctionArgument;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.functions.FunctionDefinition;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.functions.FunctionType;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * A runnable code declaration
 */
@Getter
public abstract class RunnableJavaFunction {

    private final String id;
    private final FunctionType type;
    private final List<FunctionArgument> arguments;

    public RunnableJavaFunction(@NotNull String id, @NotNull FunctionType type, @NotNull List<FunctionArgument> arguments) {
        this.id = id;
        this.type = type;
        this.arguments = arguments;
    }

    /**
     * Compute the value, using the arguments.
     * @param arguments
     * @return
     */
    public abstract Object compute(@NotNull List<Object> arguments, @NotNull SpellRuntime runtime);

    /**
     * Get the DSL definition.
     * @return a new, non-null Function Definition.
     */
    public final @NotNull FunctionDefinition getDslDefinition() {
        return new FunctionDefinition(id, type, arguments);
    }

}
