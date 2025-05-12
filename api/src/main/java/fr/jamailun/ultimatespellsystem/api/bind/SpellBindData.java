package fr.jamailun.ultimatespellsystem.api.bind;

import fr.jamailun.ultimatespellsystem.api.spells.Spell;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Duration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
     * Get the ID of the spell.
     * @return a non-null string.
     */
    default @NotNull String getSpellId() {
        return getSpell().getName();
    }

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

    /**
     * Get the bound-spell cooldown.
     * @return {@code null} if no cooldown has been set.
     */
    @Nullable Duration getCooldown();

    /**
     * If true, this cost is legacy. Only used internally with commands, no impact.
     * @return a boolean.
     */
    default boolean isLegacy() {
        return false;
    }

}
