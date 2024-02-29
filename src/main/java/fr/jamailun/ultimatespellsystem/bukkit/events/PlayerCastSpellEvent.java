package fr.jamailun.ultimatespellsystem.bukkit.events;

import fr.jamailun.ultimatespellsystem.bukkit.spells.SpellDefinition;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Event called every time a player cast a USS spell.
 */
public class PlayerCastSpellEvent extends Event implements MaybeCancellable {

    private final Player player;
    private final SpellDefinition spell;
    private boolean cancelled = false;
    private final boolean cancellable;

    public PlayerCastSpellEvent(Player player, SpellDefinition spell, boolean cancellable) {
        this.player = player;
        this.spell = spell;
        this.cancellable = cancellable;
    }

    public @NotNull SpellDefinition getSpell() {
        return spell;
    }

    public @NotNull Player getPlayer() {
        return player;
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
