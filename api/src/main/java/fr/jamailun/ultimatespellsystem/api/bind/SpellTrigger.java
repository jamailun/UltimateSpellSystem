package fr.jamailun.ultimatespellsystem.api.bind;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * A trigger for a {@link fr.jamailun.ultimatespellsystem.api.spells.Spell Spell}.
 */
public interface SpellTrigger {

    /**
     * If the trigger is made of one element.
     * @return true if {@link #getMonoTrigger()} returns something.
     * @see #getMonoTrigger()
     * @see #getTriggersList()
     */
    boolean isMonoStep();

    /**
     * Get the mono trigger.
     * @return a value if {@link #isMonoStep()} returns {@code true}.
     * @throws IllegalStateException otherwise.
     */
    @NotNull ItemBindTrigger getMonoTrigger();

    /**
     * Get the complex multistep trigger.
     * @return a list of steps if {@link #isMonoStep()} is {@code true}, or return the value of {@link #getMonoTrigger()} wrapped in a list otherwise.
     */
    @NotNull List<ItemBindTrigger> getTriggersList();

    /**
     * Get the cost of the spell.
     * @return a non-null instance of a cost.
     */
    @NotNull SpellCost getCost();

}
