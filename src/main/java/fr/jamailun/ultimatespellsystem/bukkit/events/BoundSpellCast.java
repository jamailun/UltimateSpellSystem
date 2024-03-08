package fr.jamailun.ultimatespellsystem.bukkit.events;

import fr.jamailun.ultimatespellsystem.bukkit.spells.Spell;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Event called every time a player cast a USS spell.
 */
public class BoundSpellCast extends BindingEvent implements Cancellable {

    private final Player player;
    private boolean cancelled = false;
    private boolean interactionCancelled = true;

    public BoundSpellCast(Player player, Spell spell, ItemStack boundItem) {
        super(spell, boundItem);
        this.player = player;
    }

    /**
     * Get the player casting the spell.
     * @return a non-null player, holding an item bound to a spell.
     */
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
        this.cancelled = cancelled;
    }

    /**
     * Check is the underlying interaction with the item is cancelled.
     * @return true if the interaction has been cancelled.
     */
    public boolean isInteractionCancelled() {
        return interactionCancelled;
    }

    /**
     * Set the cancel-state of the underlying interaction.
     * @param b the new state.
     */
    public void setInteractionCancelled(boolean b) {
        this.interactionCancelled = b;
    }
}
