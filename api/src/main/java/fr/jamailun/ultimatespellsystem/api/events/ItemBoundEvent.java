package fr.jamailun.ultimatespellsystem.api.events;

import fr.jamailun.ultimatespellsystem.api.spells.Spell;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Event propagated when a Spell is bound to an Item.
 */
public class ItemBoundEvent extends BindingEvent {

    /**
     * Create a new event instance.
     * @param spell spell to bind.
     * @param boundItem item to bind.
     */
    public ItemBoundEvent(@NotNull Spell spell, @NotNull ItemStack boundItem) {
        super(spell, boundItem);
    }

    private static final HandlerList HANDLERS = new HandlerList();
    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    /**
     * Bukkit boilerplate.
     * @return handlers
     */
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

}
