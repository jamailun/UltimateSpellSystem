package fr.jamailun.ultimatespellsystem.extension.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Prevent animation item from being picked-up.
 */
public class ItemPickupListener implements Listener {

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    void onEntityMove(@NotNull InventoryPickupItemEvent event) {
        if( ! event.getItem().canPlayerPickup()) {
            event.setCancelled(true);
        }
    }

}
