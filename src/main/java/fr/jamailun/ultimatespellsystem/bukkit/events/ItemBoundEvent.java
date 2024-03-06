package fr.jamailun.ultimatespellsystem.bukkit.events;

import fr.jamailun.ultimatespellsystem.bukkit.spells.SpellDefinition;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Event propagated when a Spell is bound to an Item.
 */
public class ItemBoundEvent extends BindingEvent {

    public ItemBoundEvent(SpellDefinition spell, ItemStack boundItem) {
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
