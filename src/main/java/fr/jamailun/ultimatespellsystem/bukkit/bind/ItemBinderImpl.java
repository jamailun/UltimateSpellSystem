package fr.jamailun.ultimatespellsystem.bukkit.bind;

import fr.jamailun.ultimatespellsystem.api.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.api.bukkit.bind.ItemBindException;
import fr.jamailun.ultimatespellsystem.api.bukkit.bind.ItemBinder;
import fr.jamailun.ultimatespellsystem.api.bukkit.spells.Spell;
import fr.jamailun.ultimatespellsystem.bukkit.UssKeys;
import fr.jamailun.ultimatespellsystem.api.bukkit.events.ItemBoundEvent;
import fr.jamailun.ultimatespellsystem.api.bukkit.events.ItemUnBoundEvent;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * Utils class, able to bind a {@link Spell} ID to an {@link ItemStack}.
 */
public final class ItemBinderImpl implements ItemBinder {

    @Override
    public void bind(@Nullable ItemStack item, @NotNull Spell spell, boolean destroy) throws ItemBindException {
        if(item == null) {
            throw new ItemBindException("ItemStack cannot be null.");
        }
        ItemMeta meta = item.getItemMeta();
        if(meta == null) {
            throw new ItemBindException("ItemStack doesn't have metadata.");
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

    @Override
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

    @Override
    public @NotNull Optional<String> tryFindBoundSpell(@Nullable ItemStack item) {
        if(item == null || item.getItemMeta() == null)
            return Optional.empty();
        return Optional.ofNullable(item.getItemMeta().getPersistentDataContainer().get(UssKeys.getBindKey(), PersistentDataType.STRING));
    }

    @Override
    public boolean hasDestroyKey(@Nullable ItemStack item) {
        if(item == null || item.getItemMeta() == null)
            return false;
        return item.getItemMeta().getPersistentDataContainer().has(UssKeys.getBindDestroysKey());
    }
}
