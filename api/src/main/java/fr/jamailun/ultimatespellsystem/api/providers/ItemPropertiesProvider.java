package fr.jamailun.ultimatespellsystem.api.providers;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.*;

/**
 * Read items from configuration.
 */
public final class ItemPropertiesProvider {
    private ItemPropertiesProvider() {}
    private static final Set<ItemProperty> PROPERTIES = new HashSet<>();

    /**
     * Register a new property.
     * @param property a non-null property.
     */
    public static void register(@NotNull ItemProperty property) {
        PROPERTIES.add(property);
    }

    /**
     * Get the registered properties.
     * @return a non-null and non-mutable set of properties.
     */
    public static @NotNull @UnmodifiableView Set<ItemProperty> getProperties() {
        return Collections.unmodifiableSet(PROPERTIES);
    }

    /**
     * An item context.
     * @param item reference to the Bukkit ItemStack.
     * @param meta reference to the Bukkit ItemMeta.
     * @param nbt reference to the Bukkit NBT.
     */
    public record Context(@NotNull ItemStack item, @NotNull ItemMeta meta, @NotNull PersistentDataContainer nbt) {}

    /**
     * A value provider. You can read data from it.
     */
    public interface ValueProvider {
        /**
         * Read something from the USS data.
         * @param name the name of the property.
         * @param clazz the output class to cast the value to.
         * @param defaultValue the default value to use if the key does not exist, or if the value could not be cast.
         * @return the default value or a cast output value.
         * @param <T> the output type.
         */
        <T> T get(@NotNull String name, @NotNull Class<T> clazz, @Nullable T defaultValue);
    }

    /**
     * An item property.
     */
    public interface ItemProperty {
        /**
         * Apply the property to a context.
         * @param context item context. Use it to apply change to the item, item meta, NBT, ...
         * @param provider the value provider. You can read data from it.
         */
        void apply(@NotNull Context context, @NotNull ValueProvider provider);
    }
}
