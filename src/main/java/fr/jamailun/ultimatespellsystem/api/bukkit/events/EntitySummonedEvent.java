package fr.jamailun.ultimatespellsystem.api.bukkit.events;

import fr.jamailun.ultimatespellsystem.bukkit.entities.SummonAttributesImpl;
import fr.jamailun.ultimatespellsystem.api.bukkit.entities.SpellEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * This event is called every time USS spell summons an Entity.
 */
public class EntitySummonedEvent extends Event {

    private final SummonAttributesImpl attributes;

    public EntitySummonedEvent(@NotNull SummonAttributesImpl attributes) {
        this.attributes = attributes;
    }

    /**
     * Get the entity that own the summoned entity.
     * @return a non-null bukkit Entity.
     */
    public @NotNull SpellEntity getEntity() {
        return attributes.getEntity();
    }

    /**
     * Get the entity that has been summoned.
     * @return a non-null bukkit Entity.
     */
    public @NotNull LivingEntity getSummoner() {
        return attributes.getSummoner();
    }

    /**
     * Get the attributes of this summon.
     * @return the attributes.
     */
    public @NotNull SummonAttributesImpl getAttributes() {
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