package fr.jamailun.ultimatespellsystem.bukkit.spells;

import fr.jamailun.ultimatespellsystem.bukkit.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.bukkit.events.EntityCastSpellEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * A spell is something that can be done by a caster.
 */
public abstract class Spell {

    private final String name;
    private boolean enabled = true;

    public Spell(@NotNull String name) {
        this.name = name;
    }

    /**
     * Cast a spell, in a forceful way. An event will be emitted.
     * @param entity the play to cast the spell.
     * @return {@code false} if the spell finished with an error.
     * @see EntityCastSpellEvent
     */
    public final boolean castNotCancellable(@NotNull LivingEntity entity) {
        Bukkit.getServer().getPluginManager().callEvent(new EntityCastSpellEvent(entity, this, false));
        return castSpell(entity);
    }

    /**
     * Cast a spell, in a cancellable way. An event will be emitted.
     * @param player the play to cast the spell.
     * @return {@code false} if the spell finished with an error (or if the spell did not start at all).
     * @see EntityCastSpellEvent
     */
    public final boolean cast(@NotNull Player player) {
        EntityCastSpellEvent event = new EntityCastSpellEvent(player, this, true);
        Bukkit.getServer().getPluginManager().callEvent(event);
        if (!event.isCancelled())
            return castSpell(player);
        return false;
    }

    /**
     * Cast the spell.
     * @param player the caster.
     * @return {@code true} if the spell cast properly. If {@code false}, then the spell has been dropped.
     */
    protected abstract boolean castSpell(@NotNull LivingEntity player);

    public final boolean isEnabled() {
        return enabled;
    }

    public final void setEnabled(boolean enabled) {
        this.enabled = enabled;
        UltimateSpellSystem.logInfo("Spell '" + name + "' has been " + (enabled?"enabled":"disabled") + ".");
    }

    /**
     * Get the name of the spell.
     * @return a non-null string, unique for the registry.
     */
    public final @NotNull String getName() {
        return name;
    }

}
