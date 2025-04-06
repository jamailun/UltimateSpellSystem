package fr.jamailun.ultimatespellsystem.api.bind;

import fr.jamailun.ultimatespellsystem.api.spells.Spell;
import org.jetbrains.annotations.NotNull;

/**
 * Bind data of a spell.
 */
public interface SpellBindData {

    /**
     * Get the bound spell.
     * @return a non-null spell instance.
     */
    @NotNull Spell getSpell();

    /**
     * Get the trigger configuration.
     * @return non-null instance.
     */
    @NotNull SpellTrigger getTrigger();

    /**
     * Get the spell cost of the bound spell.
     * @return the cost of the trigger.
     */
    default @NotNull SpellCost getCost() {
        return getTrigger().getCost();
    }

}
