package fr.jamailun.ultimatespellsystem.api.events;

import fr.jamailun.ultimatespellsystem.api.entities.SummonAttributes;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Event called when a summoned entity expired.
 */
@Getter
public class SummonedEntityExpiredEvent extends Event {

    @NotNull
    private final SummonAttributes summon;

    /**
     * Create a new instance.
     * @param summon summon properties.
     */
    public SummonedEntityExpiredEvent(@NotNull SummonAttributes summon) {
        this.summon = summon;
    }

    private static final HandlerList HANDLERS = new HandlerList();
    @Override
    public @NotNull HandlerList getHandlers() {return HANDLERS;}

    /**
     * Bukkit boilerplate.
     * @return handlers
     */
    public static HandlerList getHandlerList() {return HANDLERS;}
}
