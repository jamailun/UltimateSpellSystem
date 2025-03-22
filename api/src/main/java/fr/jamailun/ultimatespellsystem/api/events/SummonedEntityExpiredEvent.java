package fr.jamailun.ultimatespellsystem.api.events;

import fr.jamailun.ultimatespellsystem.api.entities.SummonAttributes;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Event called when a summoned entity expired.
 */
@RequiredArgsConstructor
@Getter
public class SummonedEntityExpiredEvent extends Event {

    @NotNull
    private final SummonAttributes summon;

    private static final HandlerList HANDLERS = new HandlerList();
    @Override
    public @NotNull HandlerList getHandlers() {return HANDLERS;}
    public static HandlerList getHandlerList() {return HANDLERS;}
}
