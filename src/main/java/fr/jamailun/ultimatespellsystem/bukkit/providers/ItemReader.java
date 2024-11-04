package fr.jamailun.ultimatespellsystem.bukkit.providers;

import fr.jamailun.ultimatespellsystem.bukkit.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.bukkit.UssKeys;
import fr.jamailun.ultimatespellsystem.bukkit.runner.SpellRuntime;
import fr.jamailun.ultimatespellsystem.bukkit.utils.StringTransformation;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class ItemReader {
    
    public static @NotNull ItemStack readFromMap(@Nullable Map<?,?> map, @NotNull SpellRuntime runtime, String context) {
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

        // Amount
        int amount = read(data, "amount", Integer.class, 1);
        int damage = read(data, "damage", Integer.class, 0);
        String name = read(data, "type", String.class, null);
        List<?> lore = read(data, "lore", List.class, null);

        UltimateSpellSystem.logInfo("DROPPABLE type = " + (map.containsKey("droppable") ? map.get("droppable") + "|" + map.get("droppable").getClass() : "null"));
        UltimateSpellSystem.logInfo("LORE type = " + (map.containsKey("lore") ? map.get("lore") + "|" + map.get("lore").getClass() : "null"));

        boolean droppable = read(data, "droppable", Boolean.class, false);

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
            PersistentDataContainer nbt = meta.getPersistentDataContainer();
            if( ! droppable) {
                nbt.set(UssKeys.getNotDroppableKey(), PersistentDataType.BOOLEAN, true);
            }
        }
        if(meta instanceof Damageable damageMeta) {
            damageMeta.setDamage(damage);
        }

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
}