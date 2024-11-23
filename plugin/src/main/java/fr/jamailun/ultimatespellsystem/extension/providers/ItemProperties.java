package fr.jamailun.ultimatespellsystem.extension.providers;

import fr.jamailun.ultimatespellsystem.UssKeys;
import fr.jamailun.ultimatespellsystem.api.providers.ItemReader;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public final class ItemProperties implements ItemReader.ItemProperty {
    private ItemProperties() {}

    public static void register() {
        ItemReader.instance().register(new ItemProperties());
    }

    @Override
    public void apply(ItemReader.@NotNull Context context, ItemReader.@NotNull ValueProvider provider) {
        boolean droppable = provider.get("droppable", Boolean.class, false);
        if(droppable) {
            context.nbt().set(UssKeys.getNotDroppableKey(), PersistentDataType.BOOLEAN, true);
        }
    }
}
