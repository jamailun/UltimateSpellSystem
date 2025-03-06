package fr.jamailun.ultimatespellsystem.plugin.listeners;

import fr.jamailun.ultimatespellsystem.UssKeys;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Apply custom damages.
 */
public class EntityDamageListener implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
    void onEntityDamage(@NotNull EntityDamageByEntityEvent event) {
        if(event.getDamager() instanceof Projectile projectile) {
            applyCustomDamages(projectile, event);
        }
    }

    private void applyCustomDamages(@NotNull Projectile projectile, @NotNull EntityDamageByEntityEvent event) {
        // Read damage
        PersistentDataContainer nbt = projectile.getPersistentDataContainer();
        if( ! nbt.has(UssKeys.getProjectileDamagesKey()))
            return;
        double damage = Objects.requireNonNull(nbt.get(UssKeys.getProjectileDamagesKey(), PersistentDataType.DOUBLE));

        // Apply to event.
        event.setDamage(damage);
    }

}
