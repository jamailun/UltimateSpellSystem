package fr.jamailun.ultimatespellsystem.api.bind;

import fr.jamailun.ultimatespellsystem.api.spells.Spell;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
     */
    void bind(@Nullable ItemStack item, @NotNull Spell spell, boolean destroy) throws ItemBindException;

    /**
     * Remove any spell-bind to an item. If no spell have been bound, do nothing.
     * @param item the item to unbind.
     */
    void unbind(@Nullable ItemStack item);

    /**
     * Try to find a spell bound to an item.
     * @param item the item to look-on.
     * @return an Optional containing the ID of the spell.
     * @deprecated legacy since post 1.5.1.
     * @see #getBindData(ItemStack)
     */
    @Deprecated
    @NotNull Optional<String> tryFindBoundSpell(@Nullable ItemStack item);

    /**
     * Check if an item should be destroyed after being used.
     * @param item the item to test. Can be null.
     * @return if the item has the "destroy key".
     * @deprecated legacy since post 1.5.1.
     * @see #getBindData(ItemStack)
     */
    @Deprecated
    boolean hasDestroyKey(@Nullable ItemStack item);

    /**
     * Get the {@link SpellBindData} of an {@link ItemStack}.
     * @param item the item to test. If {@code null}, the returned optional will be empty.
     * @return an optional of a bind-data.
     */
    @NotNull Optional<SpellBindData> getBindData(@Nullable ItemStack item);
}
