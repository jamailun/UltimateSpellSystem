package fr.jamailun.ultimatespellsystem.api.runner;

import fr.jamailun.ultimatespellsystem.api.entities.SpellEntity;
import fr.jamailun.ultimatespellsystem.api.runner.structs.Struct;
import fr.jamailun.ultimatespellsystem.api.spells.Spell;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * The execution context of a spell.
 */
public interface SpellRuntime {

    /**
     * Get the caster of the current spell.
     * @return a non-null reference to the caster.
     */
    @NotNull SpellEntity getCaster();

    /**
     * Get the spell reference of the current runtime.
     * @return a non-null spell reference. May be null when created by another plugin.
     */
    @Nullable Spell getSpell();

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
     * Break the flow.
     */
    void statementBreak();

    /**
     * Break and continue the flow.
     */
    void statementContinue();

    /**
     * Reset the 'continue' flag and resume the flow.
     */
    void acceptContinue();

    /**
     * Handle a return statement.
     * @param value output-ed value.
     */
    void statementReturn(@Nullable Object value);

    /**
     * Get the state of the control flow.
     * @return a state.
     */
    @NotNull FlowState getFlowState();

    /**
     * Create a child of this runtime.
     * @return a new instance of a runtime, with the same context.
     */
    @Contract(" -> new")
    default @NotNull SpellRuntime makeChild() {
        return makeChild(false);
    }

    /**
     * Create a child of this runtime.
     * @param function if {@code true}, the child runtime will be a function.
     * @return a new instance of a runtime, with the same context.
     */
    @Contract("_ -> new")
    @NotNull SpellRuntime makeChild(boolean function);

    /**
     * Create a child instance of this runtime, but with a different caster.
     * @param newCaster the new caster to use.
     * @return a new instance with a copy of other variables.
     */
    @Contract("_ -> new")
    @NotNull SpellRuntime makeChildNewCaster(@NotNull SpellEntity newCaster);

    /**
     * Get the structure out of a value.
     * @param structDefinitionName name of the structure of the type.
     * @param value the value to wrap.
     * @return {@code null} if cannot match.
     */
    @Contract("_,null->null;_,!null->!null")
    @Nullable Struct getStructOf(@NotNull String structDefinitionName, @Nullable Object value);

    /**
     * Get the returned value.
     * @return something.
     * @see #isStopped()
     */
    @Nullable Object getReturnedValue();

    /**
     * Evaluate a value.
     * @param expression the expression to evaluate.
     * @param clazz the output lass to cast the obtained value to.
     * @return null if the value could not be converted.
     * @param <T> output type.
     */
    <T> @Nullable T safeEvaluate(RuntimeExpression expression, Class<T> clazz);

    /**
     * Evaluate multiple values as a list.
     * @param expression the expression to evaluate.
     * @param clazz the output lass to cast the obtained value to.
     * @return an empty list if the values could not be converted.
     * @param <T> output type.
     */
    <T> @NotNull List<T> safeEvaluateList(RuntimeExpression expression, Class<T> clazz);

    /**
     * Evaluate a value. If it's a list, wrap it. If it's a single element, put it in a list.
     * @param expression the expression to evaluate.
     * @param clazz the output lass to cast the obtained value to.
     * @return an empty list if the values could not be converted.
     * @param <T> output type.
     */
    <T> @NotNull List<T> safeEvaluateAcceptsList(RuntimeExpression expression, Class<T> clazz);

}
