package fr.jamailun.ultimatespellsystem.api.events;

import fr.jamailun.ultimatespellsystem.api.entities.SpellEntity;
import fr.jamailun.ultimatespellsystem.api.spells.Spell;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Event called when an entity has been healed by a spell.
 */
@Getter
public class EntityHealedBySpellEvent extends EntityEvent implements Cancellable {

    private final @NotNull SpellEntity caster;
    private final @Nullable Spell spell;
    private double amount;
    @Setter private boolean cancelled = false;

    /**
     * New instance.
     * @param entity entity healed.
     * @param caster caster source of the health.
     * @param spell optional spell cast by the caster. Could be null if the damage is caused by another plugin for example.
     * @param amount amount of health-point to heal.
     */
    public EntityHealedBySpellEvent(
            @NotNull Entity entity,
            @NotNull SpellEntity caster,
            @Nullable Spell spell,
            double amount
    ) {
        super(entity);
        this.caster = caster;
        this.spell = spell;
        this.amount = amount;
    }

    /**
     * Set the new amount of damage done.
     * @param value a double value. Any value lower than 0 will be zero-ed.
     */
    public void setAmount(double value) {
        this.amount = Math.max(0, value);
    }

    // --------

    private static final HandlerList HANDLERS = new HandlerList();

    @Override
    public @NotNull HandlerList getHandlers() {return HANDLERS;}

    /**
     * Bukkit boilerplate.
     * @return handlers
     */
    public static HandlerList getHandlerList() {return HANDLERS;}
}
