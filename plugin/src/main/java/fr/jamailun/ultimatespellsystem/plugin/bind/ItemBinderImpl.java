package fr.jamailun.ultimatespellsystem.plugin.bind;

import fr.jamailun.ultimatespellsystem.api.bind.*;
import fr.jamailun.ultimatespellsystem.api.spells.Spell;
import fr.jamailun.ultimatespellsystem.api.events.ItemBoundEvent;
import fr.jamailun.ultimatespellsystem.api.events.ItemUnBoundEvent;
import fr.jamailun.ultimatespellsystem.UssKeys;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Utils class, able to bind a {@link Spell} ID to an {@link ItemStack}.
 */
public final class ItemBinderImpl implements ItemBinder {

    private static final SpellCostDataType TYPE = new SpellCostDataType();

    @Override
    public void bind(@Nullable ItemStack item, @NotNull Spell spell, boolean destroy) throws ItemBindException {
        ItemMeta meta = getMeta(item);
        // Write
        PersistentDataContainer nbt = meta.getPersistentDataContainer();
        nbt.set(UssKeys.getLegacyBindKey(), PersistentDataType.STRING, spell.getName());
        if(destroy) {
           nbt.set(UssKeys.getLegacyBindDestroysKey(), PersistentDataType.BOOLEAN, true);
        } else {
            nbt.remove(UssKeys.getLegacyBindDestroysKey());
        }
        item.setItemMeta(meta);
        // Propagate
        Bukkit.getPluginManager().callEvent(new ItemBoundEvent(new LegacySpellBindData(spell, destroy), item));
    }

    private @NotNull ItemMeta getMeta(@Nullable ItemStack item) throws ItemBindException{
        if(item == null) {
            throw new ItemBindException("ItemStack cannot be null.");
        }
        ItemMeta meta = item.getItemMeta();
        if(meta == null) {
            throw new ItemBindException("ItemStack doesn't have metadata.");
        }
        return meta;
    }

    @Override
    public void bind(@Nullable ItemStack item, @NotNull SpellBindData data) throws ItemBindException {
        ItemMeta meta = getMeta(item);
        meta.getPersistentDataContainer().set(
            UssKeys.getBindDataKey(),
            TYPE,
            data
        );
        item.setItemMeta(meta);
        // Propagate
        Bukkit.getPluginManager().callEvent(new ItemBoundEvent(data, item));
    }

    @Override
    public void bind(@Nullable ItemStack item, @NotNull Spell spell, @NotNull SpellTrigger trigger) throws ItemBindException {
        bind(item, new SpellBindDataImpl(spell, trigger));
    }

    @Override
    public void bind(@Nullable ItemStack item, @NotNull Spell spell, @NotNull ItemBindTrigger trigger, @NotNull SpellCost cost) throws ItemBindException {
        bind(item, spell, new SpellTriggerImpl(List.of(trigger), cost));
    }

    @Override
    public void unbind(@Nullable ItemStack item) {
        // Check if contains spell. handles null item.
        Optional<SpellBindData> dataOpt = getBindData(item);
        if(dataOpt.isEmpty())
            return;
        assert item != null;
        SpellBindData data = dataOpt.get();

        // Write
        ItemMeta meta = item.getItemMeta();
        // Legacy
        meta.getPersistentDataContainer().remove(UssKeys.getLegacyBindKey());
        meta.getPersistentDataContainer().remove(UssKeys.getLegacyBindDestroysKey());
        // Modern
        meta.getPersistentDataContainer().remove(UssKeys.getBindDataKey());
        item.setItemMeta(meta);

        // Propagate
        Bukkit.getPluginManager().callEvent(new ItemUnBoundEvent(data, item));
    }

    @Override
    public @NotNull Optional<String> tryFindBoundSpell(@Nullable ItemStack item) {
        if(item == null || item.getItemMeta() == null)
            return Optional.empty();
        return Optional.ofNullable(item.getItemMeta().getPersistentDataContainer().get(UssKeys.getLegacyBindKey(), PersistentDataType.STRING));
    }

    @Override
    public boolean hasDestroyKey(@Nullable ItemStack item) {
        if(item == null || item.getItemMeta() == null)
            return false;
        return item.getItemMeta().getPersistentDataContainer().has(UssKeys.getLegacyBindDestroysKey());
    }

    @Override
    public @NotNull Optional<SpellBindData> getBindData(@Nullable ItemStack item) {
        if(item == null || item.getItemMeta() == null)
            return Optional.empty();
        PersistentDataContainer nbt = item.getItemMeta().getPersistentDataContainer();

        // Legacy binding
        if(nbt.has(UssKeys.getLegacyBindKey(), PersistentDataType.STRING)) {
            String legacySpellId = nbt.get(UssKeys.getLegacyBindKey(), PersistentDataType.STRING);
            boolean legacyDestroyable = Objects.requireNonNullElse(nbt.get(UssKeys.getLegacyBindDestroysKey(), PersistentDataType.BOOLEAN), false);
            return Optional.of(new LegacySpellBindData(legacySpellId, legacyDestroyable));
        }

        // Modern
        SpellBindData data = nbt.get(UssKeys.getBindDataKey(), TYPE);
        return Optional.ofNullable(data);
    }
}
