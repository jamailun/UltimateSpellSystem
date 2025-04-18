package fr.jamailun.ultimatespellsystem.plugin.bind;

import fr.jamailun.ultimatespellsystem.UssLogger;
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

import java.util.ArrayList;
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
        // Get current list
        SpellBindDataContainer container = meta.getPersistentDataContainer().get(UssKeys.getBindDataKey(), TYPE);
        List<SpellBindData> itemData;
        if(container == null) {
            itemData = new ArrayList<>();
        } else {
            itemData = new ArrayList<>(container.list());
        }

        // Add this data
        itemData.add(data);

        // Write to the item
        meta.getPersistentDataContainer().set(
            UssKeys.getBindDataKey(),
            TYPE,
            new SpellBindDataContainer(itemData)
        );
        item.setItemMeta(meta);

        // Propagate an event
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
        ItemMeta meta = item == null ? null : item.getItemMeta();
        if(meta == null) return;
        var optDatas = getBindDatas(item);

        // Legacy
        meta.getPersistentDataContainer().remove(UssKeys.getLegacyBindKey());
        meta.getPersistentDataContainer().remove(UssKeys.getLegacyBindDestroysKey());
        // Modern
        meta.getPersistentDataContainer().remove(UssKeys.getBindDataKey());
        item.setItemMeta(meta);

        // Propagate event
        optDatas.ifPresent(spellBindData -> Bukkit.getPluginManager().callEvent(new ItemUnBoundEvent(item, spellBindData)));
    }

    @Override
    public void unbind(@Nullable ItemStack item, @NotNull String spellId) {
        var optDatas = getBindDatas(item);
        if(optDatas.isEmpty()) return;
        assert item != null;
        List<SpellBindData> spells = new ArrayList<>(optDatas.get());

        // Check if legacy
        SpellBindData data = spells.stream()
            .filter(d -> Objects.equals(d.getSpellId(), spellId))
            .findAny().orElse(null);
        if(data == null) return;
        ItemMeta meta = item.getItemMeta();

        if(data.isLegacy()) {
            meta.getPersistentDataContainer().remove(UssKeys.getLegacyBindKey());
            meta.getPersistentDataContainer().remove(UssKeys.getLegacyBindDestroysKey());
        } else {
            spells.remove(data);
            if(spells.isEmpty()) {
                meta.getPersistentDataContainer().remove(UssKeys.getBindDataKey());
            } else {
                meta.getPersistentDataContainer().set(
                    UssKeys.getBindDataKey(),
                    TYPE,
                    new SpellBindDataContainer(spells)
                );
            }
        }
        item.setItemMeta(meta);
        Bukkit.getPluginManager().callEvent(new ItemUnBoundEvent(item, List.of(data)));
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
    public @NotNull Optional<List<SpellBindData>> getBindDatas(@Nullable ItemStack item) {
        if(item == null || item.getItemMeta() == null)
            return Optional.empty();
        PersistentDataContainer nbt = item.getItemMeta().getPersistentDataContainer();

        // Legacy binding
        if(nbt.has(UssKeys.getLegacyBindKey(), PersistentDataType.STRING)) {
            String legacySpellId = nbt.get(UssKeys.getLegacyBindKey(), PersistentDataType.STRING);
            boolean legacyDestroyable = Objects.requireNonNullElse(nbt.get(UssKeys.getLegacyBindDestroysKey(), PersistentDataType.BOOLEAN), false);
            return Optional.of(List.of(new LegacySpellBindData(legacySpellId, legacyDestroyable)));
        }

        // Modern
        try {
            SpellBindDataContainer container = nbt.get(UssKeys.getBindDataKey(), TYPE);
            return container == null || container.list().isEmpty() ? Optional.empty() : Optional.of(container.list());
        } catch(Exception e) {
            UssLogger.logError("Error on deserialize: " + e.getMessage());
            return Optional.empty();
        }
    }
}
