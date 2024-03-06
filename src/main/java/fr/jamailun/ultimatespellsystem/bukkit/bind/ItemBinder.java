package fr.jamailun.ultimatespellsystem.bukkit.bind;

import fr.jamailun.ultimatespellsystem.bukkit.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.bukkit.events.ItemBoundEvent;
import fr.jamailun.ultimatespellsystem.bukkit.events.ItemUnBoundEvent;
import fr.jamailun.ultimatespellsystem.bukkit.spells.SpellDefinition;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.BindException;
import java.util.Optional;

/**
 * Utils class, able to bind a {@link SpellDefinition} ID to an {@link ItemStack}.
 */
public class ItemBinder {

    private final NamespacedKey key;

    /**
     * Create a new binder.
     * @param plugin the plugin to use. It will own the {@link NamespacedKey}.
     */
    public ItemBinder(Plugin plugin) {
        this.key = new NamespacedKey(plugin, "spell");
    }

    /**
     * Bind a spell to an item.
     * <br/>
     * If the binding is successful, an event will be propagated.
     * @param item the item to bind the spell to.
     * @param spell the spell to bind.
     * @throws BindException if the item instance cannot be bound.
     */
    public void bind(@Nullable ItemStack item, @NotNull SpellDefinition spell) throws BindException {
        if(item == null) {
            throw new BindException("ItemStack cannot be null.");
        }
        ItemMeta meta = item.getItemMeta();
        if(meta == null) {
            throw new BindException("ItemStack doesn't have metadata.");
        }
        // Write
        meta.getPersistentDataContainer()
            .set(key, PersistentDataType.STRING, spell.getName());
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
        if(spellIdOpt.isEmpty())
            return;

        String spellId = spellIdOpt.get();
        SpellDefinition spell = UltimateSpellSystem.getSpellsManager().getSpell(spellId);

        ItemUnBoundEvent event;
        if(spell == null) {
            event = new ItemUnBoundEvent(spellId, item);
        } else {
            event = new ItemUnBoundEvent(spell, item);
        }

        // Write
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().remove(key);
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
        return Optional.ofNullable(item.getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.STRING));
    }

    /**
     * Get the key used by this binder.
     * @return a non-null key with the 'spell' name.
     */
    public @NotNull NamespacedKey getKey() {
        return key;
    }
}
