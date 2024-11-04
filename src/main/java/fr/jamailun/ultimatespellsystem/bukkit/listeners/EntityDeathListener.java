package fr.jamailun.ultimatespellsystem.bukkit.listeners;

import fr.jamailun.ultimatespellsystem.bukkit.UssKeys;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class EntityDeathListener implements Listener {

    @EventHandler
    void entityDies(@NotNull EntityDeathEvent event) {
        event.getDrops().removeIf(this::cannotBeDropped);
    }

    private boolean cannotBeDropped(@NotNull ItemStack item) {
        if(item.getItemMeta() == null)
            return false;
        PersistentDataContainer nbt = item.getItemMeta().getPersistentDataContainer();
        Boolean cannotDrop = nbt.get(UssKeys.getNotDroppableKey(), PersistentDataType.BOOLEAN);
        return cannotDrop != null && cannotDrop;
    }

}
