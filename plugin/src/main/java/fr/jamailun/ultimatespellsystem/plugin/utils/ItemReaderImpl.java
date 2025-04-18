package fr.jamailun.ultimatespellsystem.plugin.utils;

import fr.jamailun.ultimatespellsystem.UssLogger;
import fr.jamailun.ultimatespellsystem.api.providers.ItemPropertiesProvider;
import fr.jamailun.ultimatespellsystem.api.providers.ItemPropertiesProvider.ValueProvider;
import fr.jamailun.ultimatespellsystem.api.providers.ItemPropertiesProvider.Context;
import fr.jamailun.ultimatespellsystem.api.runner.SpellRuntime;
import fr.jamailun.ultimatespellsystem.api.utils.ItemReader;
import fr.jamailun.ultimatespellsystem.api.utils.StringTransformation;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ItemReaderImpl implements ItemReader {

  @Override
  public @NotNull ItemStack readFromMap(@Nullable Map<?, ?> map, @NotNull SpellRuntime runtime, @NotNull String context) {
    Map<String, Object> data = convert(map);
    if(data == null) return new ItemStack(Material.AIR);

    // Type of the equipment
    String type = read(data, "type", String.class, Material.AIR.name());
    Material material;
    try {
      material = Material.valueOf(type.toUpperCase());
    } catch(IllegalArgumentException e) {
      UssLogger.logWarning("Illegal item for "+context+" : " + type + ".");
      material = Material.AIR;
    }

    // Read data
    int amount = read(data, "amount", Double.class, 1d).intValue();
    return readFromMap(material, amount, data, runtime);
  }

  @Override
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
      ItemPropertiesProvider.getProperties().forEach(property -> property.apply(context, provider));
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
      UssLogger.logError("Could not convert map " + map + ".");
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
