package fr.jamailun.ultimatespellsystem.api.spells;

import fr.jamailun.ultimatespellsystem.api.events.EntityCastSpellEvent;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * A spell is something that can be done by a caster.
 */
public interface Spell {

    /**
     * Cast a spell, in a forceful way. An event will be emitted.
     * @param caster the living-entity to cast the spell.
     * @return {@code false} if the spell finished with an error.
     * @see EntityCastSpellEvent
     */
    boolean castNotCancellable(@NotNull LivingEntity caster);

    /**
     * Cast a spell, in a cancellable way. An event will be emitted.
     * @param player the player to cast the spell.
     * @return {@code false} if the spell finished with an error (or if the spell did not start at all).
     * @see EntityCastSpellEvent
     */
    boolean cast(@NotNull Player player);

    /**
     * Test if this spell has been enabled, i.e. is <i>castable</i>.
     * @return true if the spell can be cast.
     */
    boolean isEnabled();

    /**
     * Set if the spell is enabled. If true, can be cast.
     * @param enabled the value to use.
     */
    void setEnabled(boolean enabled);

    /**
     * Get the name of the spell.
     * @return a non-null string, unique for the registry.
     */
    @NotNull String getName();

    @NotNull String getDebugString();

}
