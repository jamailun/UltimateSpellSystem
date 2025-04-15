package fr.jamailun.ultimatespellsystem.api.bind;

import fr.jamailun.ultimatespellsystem.api.spells.Spell;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Utils class, able to bind a {@link Spell} ID to an {@link ItemStack}.
 */
public interface ItemBinder {

    /**
     * Bind a spell to an item.
     * <br/>
     * If the binding is successful, an event will be propagated.
     * @param item the item to bind the spell to.
     * @param spell the spell to bind.
     * @throws ItemBindException if the item instance cannot be bound.
     * @deprecated legacy since 1.6.0.
     */
    @Deprecated(since = "1.6.0", forRemoval = true)
    void bind(@Nullable ItemStack item, @NotNull Spell spell, boolean destroy) throws ItemBindException;

    /**
     * Bind multiple spell data to an item.
     * @param item the item to bind. If null, will do nothing.
     * @param dataCollection a non-null collection of spell data to bind.
     * @throws ItemBindException if the item instance cannot be bound.
     */
    default void bind(@Nullable ItemStack item, @NotNull Collection<SpellBindData> dataCollection) throws ItemBindException {
        for(SpellBindData data : dataCollection) {
            bind(item, data);
        }
    }

    /**
     * Bind a spell data to an item.
     * @param item the item to bind. If null, will do nothing.
     * @param data spell data to bind.
     * @throws ItemBindException if the item instance cannot be bound.
     */
    void bind(@Nullable ItemStack item, @NotNull SpellBindData data) throws ItemBindException;

    /**
     * Bind a spell data to an item.
     * @param item the item to bind. If null, will do nothing.
     * @param spell spell to bind.
     * @param trigger trigger to use for the binding.
     * @throws ItemBindException if the item instance cannot be bound.
     */
    void bind(@Nullable ItemStack item, @NotNull Spell spell, @NotNull SpellTrigger trigger) throws ItemBindException;

    /**
     * Create a basic spell data binding.
     * @param item the item to bind. If null, will do nothing.
     * @param spell the spell to bind to the item.
     * @param trigger the action to trigger the spell.
     * @param cost the cost of the trigger.
     * @throws ItemBindException if the item instance cannot be bound.
     */
    void bind(@Nullable ItemStack item, @NotNull Spell spell, @NotNull ItemBindTrigger trigger, @NotNull SpellCost cost) throws ItemBindException;

    /**
     * Remove <b>all</b> spells bound to an item. If no spell have been bound, does nothing.
     * @param item the item to unbind. Does nothing if null.
     */
    void unbind(@Nullable ItemStack item);

    /**
     * Remove a specific bound spell from an item. If this spell does is not bound, does nothing.
     * @param item the item to unbind. Does nothing if null.
     * @param spellId the non-null spell ID to remove.
     */
    void unbind(@Nullable ItemStack item, @NotNull String spellId);

    /**
     * Try to find a spell bound to an item.
     * @param item the item to look-on.
     * @return an Optional containing the ID of the spell.
     * @deprecated Use the complete {@link #getBindDatas(ItemStack)}.
     * @see #getBindDatas(ItemStack)
     */
    @Deprecated(since = "1.6.0", forRemoval = true)
    @NotNull Optional<String> tryFindBoundSpell(@Nullable ItemStack item);

    /**
     * Check if an item should be destroyed after being used.
     * @param item the item to test. Can be null.
     * @return if the item has the "destroy key".
     * @deprecated Use the complete {@link #getBindDatas(ItemStack)}.
     */
    @Deprecated(since = "1.6.0", forRemoval = true)
    boolean hasDestroyKey(@Nullable ItemStack item);

    /**
     * Get the {@link SpellBindData} of an {@link ItemStack}.
     * @param item the item to test. If {@code null}, the returned optional will be empty.
     * @return an optional of a bind-data.
     */
    @NotNull Optional<List<SpellBindData>> getBindDatas(@Nullable ItemStack item);
}
