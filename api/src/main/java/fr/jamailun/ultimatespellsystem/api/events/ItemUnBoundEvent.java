package fr.jamailun.ultimatespellsystem.api.events;

import fr.jamailun.ultimatespellsystem.api.bind.SpellBindData;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Event propagated when a Spell is unbound from an Item.
 */
@Getter
public class ItemUnBoundEvent extends Event {

    private final ItemStack boundItem;
    private final List<SpellBindData> datas;

    /**
     * New event with the spell instance.
     * @param datas the unbound data.
     * @param boundItem the unbound item.
     */
    public ItemUnBoundEvent(@NotNull ItemStack boundItem, @NotNull List<SpellBindData> datas) {
         this.boundItem = boundItem;
         this.datas = datas;
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
