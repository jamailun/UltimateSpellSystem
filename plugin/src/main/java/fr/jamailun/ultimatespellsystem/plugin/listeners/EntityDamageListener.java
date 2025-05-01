package fr.jamailun.ultimatespellsystem.plugin.listeners;

import fr.jamailun.ultimatespellsystem.api.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.api.entities.SummonAttributes;
import fr.jamailun.ultimatespellsystem.api.providers.AlliesProvider;
import fr.jamailun.ultimatespellsystem.api.providers.SummonPropertiesProvider;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Apply custom damages.
 */
public class EntityDamageListener implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
    void onEntityDamage(@NotNull EntityDamageByEntityEvent event) {
        // Only listen for projectiles.
        if(!(event.getDamager() instanceof Projectile projectile))
            return;

        // Only care for summoned projectiles
        SummonAttributes summon = UltimateSpellSystem.getSummonsManager().find(projectile.getUniqueId());
        if(summon == null)
            return;

        // Cancel event if the projectile should not apply damage to the target.
        if(shouldCancelDamage(summon, event.getEntity())) {
            event.setCancelled(true);
            return;
        }

        // Don't do anything if the 'projectile_damage' attribute is not set. The default velocity computed damage will apply.
        if(summon.getAttribute("projectile_damage") instanceof Double dmg)
            applyDamage(event, dmg);
    }

    private boolean shouldCancelDamage(@NotNull SummonAttributes summon, @NotNull Entity target) {
        // Cannot hit caster ?
        if( ! summon.tryGetAttribute(SummonPropertiesProvider.ATTRIBUTE_PROJECTILE_CAN_DAMAGE_CASTER, Boolean.class, true)) {
            if(Objects.equals(summon.getSummoner().getUniqueId(), target.getUniqueId())) {
                return true;
            }
        }

        // Cannot hit caster allies ?
        if( ! summon.tryGetAttribute(SummonPropertiesProvider.ATTRIBUTE_PROJECTILE_CAN_DAMAGE_ALLIES, Boolean.class, true)) {
            if(AlliesProvider.instance().testForAllies(summon.getSummoner(), target) == AlliesProvider.AlliesResult.ALLIES) {
                return true;
            }
        }
        return false;
    }

    private void applyDamage(@NotNull EntityDamageByEntityEvent event, double dmg) {
        // Don't do anything if no damage
        if(dmg == 0) {
            event.setCancelled(true);
        }
        // Heal for negative damage
        else if(dmg < 0) {
            if(event.getEntity() instanceof LivingEntity living) {
                living.heal(-dmg);
            }
            event.setDamage(0);
        }
        // Just set damage value otherwise
        else {
            event.setDamage(dmg);
        }
    }

}
