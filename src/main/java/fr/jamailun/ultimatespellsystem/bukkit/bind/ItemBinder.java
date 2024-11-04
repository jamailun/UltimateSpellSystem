package fr.jamailun.ultimatespellsystem.bukkit.bind;

import fr.jamailun.ultimatespellsystem.bukkit.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.bukkit.UssKeys;
import fr.jamailun.ultimatespellsystem.bukkit.events.ItemBoundEvent;
import fr.jamailun.ultimatespellsystem.bukkit.events.ItemUnBoundEvent;
import fr.jamailun.ultimatespellsystem.bukkit.spells.Spell;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.BindException;
import java.util.Optional;

/**
 * Utils class, able to bind a {@link Spell} ID to an {@link ItemStack}.
 */
public class ItemBinder {

    /**
     * Bind a spell to an item.
     * <br/>
     * If the binding is successful, an event will be propagated.
     * @param item the item to bind the spell to.
     * @param spell the spell to bind.
     * @throws BindException if the item instance cannot be bound.
     */
    public void bind(@Nullable ItemStack item, @NotNull Spell spell, boolean destroy) throws BindException {
        if(item == null) {
            throw new BindException("ItemStack cannot be null.");
        }
        ItemMeta meta = item.getItemMeta();
        if(meta == null) {
            throw new BindException("ItemStack doesn't have metadata.");
        }
        // Write
        PersistentDataContainer nbt = meta.getPersistentDataContainer();
        nbt.set(UssKeys.getBindKey(), PersistentDataType.STRING, spell.getName());
        if(destroy) {
           nbt.set(UssKeys.getBindDestroysKey(), PersistentDataType.BOOLEAN, true);
        } else {
            nbt.remove(UssKeys.getBindDestroysKey());
        }
        item.setItemMeta(meta);
        // Propagate
        Bukkit.getPluginManager().callEvent(new ItemBoundEvent(spell, item));
    }

    /**
     * Remove any spell-bind to an item. If no spell have been bound, do nothing.
     * @param item the item to unbind.
     */
    public void unbind(@Nullable ItemStack item) {
        // Check if contains spell. handles null item.
        Optional<String> spellIdOpt = tryFindBoundSpell(item);
        if(item == null || spellIdOpt.isEmpty())
            return;

        String spellId = spellIdOpt.get();
        Spell spell = UltimateSpellSystem.getSpellsManager().getSpell(spellId);

        ItemUnBoundEvent event;
        if(spell == null) {
            event = new ItemUnBoundEvent(spellId, item);
        } else {
            event = new ItemUnBoundEvent(spell, item);
        }

        // Write
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().remove(UssKeys.getBindKey());
        meta.getPersistentDataContainer().remove(UssKeys.getBindDestroysKey());
        item.setItemMeta(meta);

        // Propagate
        Bukkit.getPluginManager().callEvent(event);
    }

    /**
     * Try to find a spell bound to an item.
     * @param item the item to look-on.
     * @return an Optional containing the ID of the spell.
     */
    public @NotNull Optional<String> tryFindBoundSpell(@Nullable ItemStack item) {
        if(item == null || item.getItemMeta() == null)
            return Optional.empty();
        return Optional.ofNullable(item.getItemMeta().getPersistentDataContainer().get(UssKeys.getBindKey(), PersistentDataType.STRING));
    }

    /**
     * Check if an item should be destroyed after being used.
     * @param item the item to test. Can be null.
     * @return if the item has the "destroy key".
     */
    public boolean hasDestroyKey(@Nullable ItemStack item) {
        if(item == null || item.getItemMeta() == null)
            return false;
        return item.getItemMeta().getPersistentDataContainer().has(UssKeys.getBindDestroysKey());
    }
}
