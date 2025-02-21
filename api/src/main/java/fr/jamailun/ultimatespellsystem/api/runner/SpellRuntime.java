package fr.jamailun.ultimatespellsystem.api.runner;

import fr.jamailun.ultimatespellsystem.api.utils.MultivaluedMap;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * The execution context of a spell.
 */
public interface SpellRuntime {

    /**
     * Get the caster of the current spell.
     * @return a non-null reference to the caster.
     */
    @NotNull LivingEntity getCaster();

    /**
     * A reference to the variables set.
     * @return the final reference to the variables.
     */
    @NotNull VariablesSet variables();

    /**
     * Test if the runtime has been stopped.
     * @return true if no more statements should be executed.
     */
    boolean isStopped();

    /**
     * Stop the current execution.
     * @param exitCode the exit code to use.
     */
    void stop(int exitCode);

    /**
     * Create a child of this runtime.
     * @return a new instance of a runtime, with the same context.
     */
    @Contract(" -> new")
    @NotNull SpellRuntime makeChild();

    /**
     * Get the final exit code.
     * @return {@code zero} if it's a success (or not stopped). Any other value means an error occurred.
     * @see #isStopped()
     */
    int getFinalExitCode();


    <T> T safeEvaluate(RuntimeExpression expression, Class<T> clazz);
    <T> List<T> safeEvaluateList(RuntimeExpression expression, Class<T> clazz);
    <T> List<T> safeEvaluateAcceptsList(RuntimeExpression expression, Class<T> clazz);

}
