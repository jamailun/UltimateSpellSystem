package fr.jamailun.ultimatespellsystem.extension.providers;

import fr.jamailun.ultimatespellsystem.UssKeys;
import fr.jamailun.ultimatespellsystem.api.providers.ItemPropertiesProvider;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

/**
 * Add custom Item properties.
 */
public final class ItemProperties implements ItemPropertiesProvider.ItemProperty {
    private ItemProperties() {}

    public static void register() {
        ItemPropertiesProvider.register(new ItemProperties());
    }

    @Override
    public void apply(ItemPropertiesProvider.@NotNull Context context, ItemPropertiesProvider.@NotNull ValueProvider provider) {
        boolean droppable = provider.get("droppable", Boolean.class, false);
        if(droppable) {
            context.nbt().set(UssKeys.getNotDroppableKey(), PersistentDataType.BOOLEAN, true);
        }
    }
}
