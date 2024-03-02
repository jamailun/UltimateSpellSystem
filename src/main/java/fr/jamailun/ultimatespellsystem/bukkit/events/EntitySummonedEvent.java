package fr.jamailun.ultimatespellsystem.bukkit.events;

import fr.jamailun.ultimatespellsystem.bukkit.entities.SummonAttributes;
import fr.jamailun.ultimatespellsystem.bukkit.spells.SpellEntity;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * This event is called every time USS spell summons an Entity.
 */
public class EntitySummonedEvent extends Event {

    private final SummonAttributes attributes;

    public EntitySummonedEvent(SummonAttributes attributes) {
        this.attributes = attributes;
    }

    public SpellEntity getEntity() {
        return attributes.getEntity();
    }

    public Entity getSummoner() {
        return attributes.getSummoner();
    }

    public SummonAttributes getAttributes() {
        return attributes;
    }

    private static final HandlerList HANDLERS = new HandlerList();
    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
