package fr.jamailun.ultimatespellsystem.api.providers;

import fr.jamailun.ultimatespellsystem.api.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.api.runner.SpellRuntime;
import fr.jamailun.ultimatespellsystem.api.utils.StringTransformation;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * Read items from configuration.
 */
public final class ItemReader {
    private ItemReader() {}
    private static final ItemReader INSTANCE = new ItemReader();
    private final Set<ItemProperty> properties = new HashSet<>();

    public void register(@NotNull ItemProperty property) {
        properties.add(property);
    }

    /**
     * Get the instance.
     * @return the non-null, existing instance.
     */
    public static @NotNull ItemReader instance() {
        return INSTANCE;
    }

    /**
     * Read an item from a data-map.
     * @param map the map to use. {@code Must be String - Any}
     * @param runtime the context to use.
     * @param context the string context to print on errors.
     * @return a non-null item. The material will be {@code AIR} if a problem occurred (or if type not defined).
     */
    public @NotNull ItemStack readFromMap(@Nullable Map<?,?> map, @NotNull SpellRuntime runtime, String context) {
        Map<String, Object> data = convert(map);
        if(data == null) return new ItemStack(Material.AIR);

        // Type of the equipment
        String type = read(data, "type", String.class, Material.AIR.name());
        Material material;
        try {
            material = Material.valueOf(type.toUpperCase());
        } catch(IllegalArgumentException e) {
            UltimateSpellSystem.logWarning("Illegal item for "+context+" : " + type + ".");
            material = Material.AIR;
        }

        // Read data
        int amount = read(data, "amount", Double.class, 1d).intValue();
        return readFromMap(material, amount, data, runtime);
    }

    /**
     * Read an item from a data-map, with some elements already set.
     * @param material the material of the item.
     * @param amount the amount to use.
     * @param data the data-map to use.
     * @param runtime the context to use.
     * @return a non-null item. The material will be {@code AIR} if a problem occurred.
     */
    public @NotNull ItemStack readFromMap(@NotNull Material material, int amount, @NotNull Map<String, Object> data, @NotNull SpellRuntime runtime) {
        int damage = read(data, "damage", Integer.class, 0);
        String name = read(data, "name", String.class, null);
        List<?> lore = read(data, "lore", List.class, null);
        boolean unbreakable = read(data, "unbreakable", Boolean.class, false);

        // Apply data to item meta
        ItemStack item = new ItemStack(material, amount);
        ItemMeta meta = item.getItemMeta();
        if(meta != null) {
            if(name != null)
                meta.displayName(StringTransformation.parse(StringTransformation.transformString(name, runtime)));
            if(lore != null)
                meta.lore(
                        lore.stream()
                                .filter(Objects::nonNull)
                                .map(Objects::toString)
                                .map(line -> StringTransformation.parse("&7&o" + StringTransformation.transformString(line, runtime)))
                                .toList()
                );
            // Apply properties
            Context context = new Context(item, meta, meta.getPersistentDataContainer());
            ValueProvider provider = new ValueProvider() {
                @Override
                public <T> T get(@NotNull String name, @NotNull Class<T> clazz, @Nullable T defaultValue) {
                    return read(data, name, clazz, defaultValue);
                }
            };
            for(ItemProperty property : properties) {
                property.apply(context, provider);
            }
        }
        if(meta instanceof Damageable damageMeta) {
            damageMeta.setDamage(damage);
            damageMeta.setUnbreakable(unbreakable);
        }

        // Set and return
        item.setItemMeta(meta);
        return item;
    }

    @SuppressWarnings("unchecked")
    private static @Nullable Map<String, Object> convert(@Nullable Map<?,?> map) {
        if(map == null) return null;
        try {
            return (Map<String, Object>) map;
        } catch(ClassCastException e) {
            UltimateSpellSystem.logError("Could not convert map " + map + ".");
            return null;
        }
    }

    private static <T> T read(@NotNull Map<String,Object> map, @NotNull String key, @NotNull Class<T> clazz, @Nullable T defaultValue) {
        Object object = map.get(key);
        if(object == null) return defaultValue;
        try {
            return clazz.cast(object);
        } catch(ClassCastException e) {
            return defaultValue;
        }
    }

    public record Context(@NotNull ItemStack item, @NotNull ItemMeta meta, @NotNull PersistentDataContainer nbt) {}

    public interface ValueProvider {
        <T> T get(@NotNull String name, @NotNull Class<T> clazz, @Nullable T defaultValue);
    }

    public interface ItemProperty {
        void apply(@NotNull Context context, @NotNull ValueProvider provider);
    }
}
