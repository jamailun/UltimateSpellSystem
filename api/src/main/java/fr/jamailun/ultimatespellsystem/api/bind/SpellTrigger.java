package fr.jamailun.ultimatespellsystem.api.bind;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * A trigger for a {@link fr.jamailun.ultimatespellsystem.api.spells.Spell Spell}.
 */
public interface SpellTrigger {

    /**
     * Get the complex multistep trigger.
     * @return a list of steps to trigger this spell.
     */
    @NotNull List<ItemBindTrigger> getTriggersList();

    /**
     * Get the cost of the spell.
     * @return a non-null instance of a cost.
     */
    @NotNull SpellCost getCost();

}
