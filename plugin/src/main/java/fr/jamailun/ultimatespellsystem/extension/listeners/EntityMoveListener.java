package fr.jamailun.ultimatespellsystem.extension.listeners;

import fr.jamailun.ultimatespellsystem.UssKeys;
import io.papermc.paper.event.entity.EntityMoveEvent;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

/**
 * Forbid movement with the proper namespacedKey.
 */
public class EntityMoveListener implements Listener {

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    void onEntityMove(@NotNull EntityMoveEvent event) {
        if(shouldHandle(event.getEntity())) {
            event.setCancelled(true);
        }
    }

    private boolean shouldHandle(@NotNull Entity entity) {
        PersistentDataContainer nbt = entity.getPersistentDataContainer();
        Boolean value = nbt.get(UssKeys.custom("cancel-movement"), PersistentDataType.BOOLEAN);
        return value != null && value;
    }

}
