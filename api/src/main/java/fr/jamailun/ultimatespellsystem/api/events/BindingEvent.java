package fr.jamailun.ultimatespellsystem.api.events;

import fr.jamailun.ultimatespellsystem.api.bind.SpellBindData;
import fr.jamailun.ultimatespellsystem.api.spells.Spell;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * An event about a binding of a spell.
 */
public abstract class BindingEvent extends Event {
    private final SpellBindData data;
    private final ItemStack boundItem;

    /**
     * A new event instance.
     * @param data bound data.
     * @param boundItem item bound.
     */
    public BindingEvent(@NotNull SpellBindData data, @NotNull ItemStack boundItem) {
        this.data = data;
        this.boundItem = boundItem;
    }

    /**
     * Get the spell ID, used by this event.
     * @return a non-null spell identifier.
     */
    public @NotNull final String getSpellId() {
        return getSpell().getName();
    }

    /**
     * Get the spell ID, used by this event.
     * @return a non-null spell definition.
     */
    public @NotNull Spell getSpell() {
        return data.getSpell();
    }

    /**
     * Get the item about this event.
     * @return a non-null item, with an item-meta.
     */
    public @NotNull final ItemStack getItem() {
        return boundItem;
    }

}
