package fr.jamailun.ultimatespellsystem.plugin.bind;

import fr.jamailun.ultimatespellsystem.UssLogger;
import fr.jamailun.ultimatespellsystem.api.bind.*;
import fr.jamailun.ultimatespellsystem.api.spells.Spell;
import fr.jamailun.ultimatespellsystem.api.events.ItemBoundEvent;
import fr.jamailun.ultimatespellsystem.api.events.ItemUnBoundEvent;
import fr.jamailun.ultimatespellsystem.UssKeys;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Duration;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * Utils class, able to bind a {@link Spell} ID to an {@link ItemStack}.
 */
public final class ItemBinderImpl implements ItemBinder {

    private static final LegacySpellBindDataType TYPE_LEGACY = new LegacySpellBindDataType();
    private static final SpellBindDataType TYPE = new SpellBindDataType();

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
    public void bind(@Nullable ItemStack item, @NotNull Spell spell, @NotNull SpellTrigger trigger) throws ItemBindException {
        bind(item, new SpellBindDataImpl(spell, trigger, null));
    }

    @Override
    public void bind(@Nullable ItemStack item, @NotNull Spell spell, @NotNull ItemBindTrigger trigger, @NotNull SpellCost cost) throws ItemBindException {
        bind(item, spell, trigger, cost, null);
    }

    @Override
    public void bind(@Nullable ItemStack item, @NotNull Spell spell, @NotNull ItemBindTrigger trigger, @NotNull SpellCost cost, @Nullable Duration cooldown) throws ItemBindException {
        bind(item, new SpellBindDataImpl(spell, new SpellTriggerImpl(List.of(trigger), cost), cooldown));
    }

    @Override
    public void bind(@Nullable ItemStack item, @NotNull SpellBindData data) throws ItemBindException {
        // Get current list
        List<SpellBindData> previous = getBindDatas(item).orElseGet(Collections::emptyList);
        List<SpellBindData> itemData = new ArrayList<>(previous);

        // Add this data
        ItemMeta meta = getMeta(item);
        itemData.add(data);

        // Remove V1
        meta.getPersistentDataContainer().remove(UssKeys.getBindDataKeyV1());

        // Write to the item
        meta.getPersistentDataContainer().set(
            UssKeys.getBindDataKeyV2(),
            TYPE,
            new SpellBindDataContainer(itemData)
        );
        item.setItemMeta(meta);

        // Propagate an event
        Bukkit.getPluginManager().callEvent(new ItemBoundEvent(data, item));
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
        meta.getPersistentDataContainer().remove(UssKeys.getBindDataKeyV1());
        // Modern 2
        meta.getPersistentDataContainer().remove(UssKeys.getBindDataKeyV2());
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
            meta.getPersistentDataContainer().remove(UssKeys.getBindDataKeyV1()); // removeV1

            if(spells.isEmpty()) {
                // If not more data, we remove the key
                meta.getPersistentDataContainer().remove(UssKeys.getBindDataKeyV2());
            } else {
                meta.getPersistentDataContainer().set(
                    UssKeys.getBindDataKeyV2(),
                    TYPE,
                    new SpellBindDataContainer(spells)
                );
            }
        }
        item.setItemMeta(meta);
        Bukkit.getPluginManager().callEvent(new ItemUnBoundEvent(item, List.of(data)));
    }

    @Override
    public @NotNull Optional<List<SpellBindData>> getBindDatas(@Nullable ItemStack item) {
        if(item == null || item.getItemMeta() == null)
            return Optional.empty();
        PersistentDataContainer nbt = item.getItemMeta().getPersistentDataContainer();

        // Legacy binding
        if(nbt.has(UssKeys.getLegacyBindKey())) {
            String legacySpellId = nbt.get(UssKeys.getLegacyBindKey(), PersistentDataType.STRING);
            boolean legacyDestroyable = Objects.requireNonNullElse(nbt.get(UssKeys.getLegacyBindDestroysKey(), PersistentDataType.BOOLEAN), false);
            return Optional.of(List.of(new LegacySpellBindData(legacySpellId, legacyDestroyable)));
        }

        // V1
        if(nbt.has(UssKeys.getBindDataKeyV1())) {
            try {
                SpellBindDataContainer container = nbt.get(UssKeys.getBindDataKeyV1(), TYPE_LEGACY);
                return container == null || container.list().isEmpty() ? Optional.empty() : Optional.of(container.list());
            } catch(Exception e) {
                UssLogger.logError("Error on deserialize (v1): " + e.getMessage());
                return Optional.empty();
            }
        }

        // V2
        try {
            SpellBindDataContainer container = nbt.get(UssKeys.getBindDataKeyV2(), TYPE);
            return container == null || container.list().isEmpty() ? Optional.empty() : Optional.of(container.list());
        } catch(Exception e) {
            UssLogger.logError("Error on deserialize (v2): " + e.getMessage(), e);
            return Optional.empty();
        }
    }
}
