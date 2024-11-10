package fr.jamailun.ultimatespellsystem.api.bukkit.events;

import fr.jamailun.ultimatespellsystem.api.bukkit.spells.Spell;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Event propagated when a Spell is unbound from an Item.
 */
public class ItemUnBoundEvent extends BindingEvent {

    public ItemUnBoundEvent(@NotNull String spellId, @NotNull ItemStack boundItem) {
        super(spellId, boundItem);
    }
    public ItemUnBoundEvent(@NotNull Spell spell, @NotNull ItemStack boundItem) {
        super(spell, boundItem);
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
