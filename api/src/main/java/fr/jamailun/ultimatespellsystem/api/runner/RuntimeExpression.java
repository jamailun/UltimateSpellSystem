package fr.jamailun.ultimatespellsystem.api.runner;

import org.jetbrains.annotations.NotNull;

/**
 * The runtime-version of an expression. Can be evaluated with a {@link SpellRuntime}.
 */
public abstract class RuntimeExpression {

    /**
     * Evaluate this expression node with a specific context.
     * @param runtime the runtime-context to use.
     * @return the value generated by the expression.
     */
    public abstract Object evaluate(@NotNull SpellRuntime runtime);

}