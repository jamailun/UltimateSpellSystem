package fr.jamailun.ultimatespellsystem.api.events;

import fr.jamailun.ultimatespellsystem.api.spells.Spell;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Event propagated when a Spell is unbound from an Item.
 */
public class ItemUnBoundEvent extends BindingEvent {

    /**
     * New event without the spell instance.
     * @param spellId the spell ID.
     * @param boundItem the unbound item.
     */
    public ItemUnBoundEvent(@NotNull String spellId, @NotNull ItemStack boundItem) {
        super(spellId, boundItem);
    }

    /**
     * New event with the spell instance.
     * @param spell the spell instance.
     * @param boundItem the unbound item.
     */
    public ItemUnBoundEvent(@NotNull Spell spell, @NotNull ItemStack boundItem) {
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
