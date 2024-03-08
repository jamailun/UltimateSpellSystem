package fr.jamailun.ultimatespellsystem.bukkit.events;

import fr.jamailun.ultimatespellsystem.bukkit.spells.Spell;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Event propagated when a Spell is unbound from an Item.
 */
public class ItemUnBoundEvent extends BindingEvent {

    public ItemUnBoundEvent(String spellId, ItemStack boundItem) {
        super(spellId, boundItem);
    }
    public ItemUnBoundEvent(Spell spell, ItemStack boundItem) {
        super(spell, boundItem);
    }

    /**
     * Get the spell ID, used by this event.
     * @return null if the previously bound spell was unknown.
     * @see #getSpellId()
     */
    @Override
    public @Nullable Spell getSpell() {
        return spell;
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
