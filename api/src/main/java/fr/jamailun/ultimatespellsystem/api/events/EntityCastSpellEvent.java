package fr.jamailun.ultimatespellsystem.api.events;

import fr.jamailun.ultimatespellsystem.api.spells.Spell;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Event called every time a player cast a USS spell.
 */
public class EntityCastSpellEvent extends Event implements MaybeCancellable {

    private final LivingEntity caster;
    private final Spell spell;
    private boolean cancelled = false;
    private final boolean cancellable;

    /**
     * Create a new event instance.
     * @param caster the caster to use.
     * @param spell spell to be cast.
     * @param cancellable true if the spell can be cancelled.
     */
    public EntityCastSpellEvent(@NotNull LivingEntity caster, @NotNull Spell spell, boolean cancellable) {
        this.caster = caster;
        this.spell = spell;
        this.cancellable = cancellable;
    }

    /**
     * Get the spell instance.
     * @return a non-null spell instance.
     */
    public @NotNull Spell getSpell() {
        return spell;
    }

    /**
     * Get the caster
     * @return the reference to the bukkit caster.
     */
    public @NotNull LivingEntity getCaster() {
        return caster;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        if(!cancellable)
            return;
        this.cancelled = cancelled;
    }

    @Override
    public boolean isCancellable() {
        return cancellable;
    }

    private static final HandlerList HANDLERS = new HandlerList();

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    /**
     * Boilerplate
     * @return handlers.
     */
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

}
