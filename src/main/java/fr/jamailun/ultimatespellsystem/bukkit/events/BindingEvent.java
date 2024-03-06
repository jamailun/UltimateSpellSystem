package fr.jamailun.ultimatespellsystem.bukkit.events;

import fr.jamailun.ultimatespellsystem.bukkit.spells.SpellDefinition;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * An event about a binding of a spell.
 */
public abstract class BindingEvent extends Event {
    private final String spellId;
    protected final SpellDefinition spell;
    private final ItemStack boundItem;

    public BindingEvent(SpellDefinition spell, ItemStack boundItem) {
        this.spell = spell;
        this.spellId = spell.getName();
        this.boundItem = boundItem;
    }
    public BindingEvent(String spellId, ItemStack boundItem) {
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
    public SpellDefinition getSpell() {
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
