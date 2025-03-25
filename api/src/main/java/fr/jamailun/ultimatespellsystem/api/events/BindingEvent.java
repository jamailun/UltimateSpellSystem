package fr.jamailun.ultimatespellsystem.api.events;

import fr.jamailun.ultimatespellsystem.api.spells.Spell;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * An event about a binding of a spell.
 */
public abstract class BindingEvent extends Event {
    private final String spellId;
    protected final Spell spell;
    private final ItemStack boundItem;

    /**
     * A new event instance.
     * @param spell bound spell.
     * @param boundItem item bound.
     */
    public BindingEvent(@NotNull Spell spell, @NotNull ItemStack boundItem) {
        this.spell = spell;
        this.spellId = spell.getName();
        this.boundItem = boundItem;
    }

    /**
     * A new event instance.
     * @param spellId ID of the spell.
     * @param boundItem item bound.
     */
    public BindingEvent(@NotNull String spellId, @NotNull ItemStack boundItem) {
        this.spellId = spellId;
        this.spell = null;
        this.boundItem = boundItem;
    }

    /**
     * Get the spell ID, used by this event.
     * @return a non-null spell identifier.
     */
    public @NotNull final String getSpellId() {
        return spellId;
    }

    /**
     * Get the spell ID, used by this event.
     * @return a non-null spell definition.
     */
    public @Nullable Spell getSpell() {
        return spell;
    }

    /**
     * Get the item about this event.
     * @return a non-null item, with an item-meta.
     */
    public @NotNull final ItemStack getItem() {
        return boundItem;
    }

}
