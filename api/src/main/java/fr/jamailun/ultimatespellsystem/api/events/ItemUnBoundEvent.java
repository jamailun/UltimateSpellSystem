package fr.jamailun.ultimatespellsystem.api.events;

import fr.jamailun.ultimatespellsystem.api.bind.SpellBindData;
import fr.jamailun.ultimatespellsystem.api.spells.Spell;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Event propagated when a Spell is unbound from an Item.
 */
public class ItemUnBoundEvent extends BindingEvent {

    /**
     * New event with the spell instance.
     * @param data the unbound data.
     * @param boundItem the unbound item.
     */
    public ItemUnBoundEvent(@NotNull SpellBindData data, @NotNull ItemStack boundItem) {
         super(data, boundItem);
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
