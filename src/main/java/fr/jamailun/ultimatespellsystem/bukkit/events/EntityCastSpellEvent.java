package fr.jamailun.ultimatespellsystem.bukkit.events;

import fr.jamailun.ultimatespellsystem.bukkit.spells.Spell;
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

    public EntityCastSpellEvent(LivingEntity caster, Spell spell, boolean cancellable) {
        this.caster = caster;
        this.spell = spell;
        this.cancellable = cancellable;
    }

    public @NotNull Spell getSpell() {
        return spell;
    }

    public @NotNull LivingEntity getCaster() {
        return caster;
    }

    private static final HandlerList HANDLERS = new HandlerList();
    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }
    public static HandlerList getHandlerList() {
        return HANDLERS;
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

}
