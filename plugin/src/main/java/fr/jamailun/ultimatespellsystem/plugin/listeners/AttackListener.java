package fr.jamailun.ultimatespellsystem.plugin.listeners;

import fr.jamailun.ultimatespellsystem.api.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.api.bind.ItemBindTrigger;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Listen to attacks and trigger when required.
 */
public class AttackListener implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    void playerLeftClick(@NotNull EntityDamageByEntityEvent event) {
        if(event.getDamager() instanceof Player player) {
            if(UltimateSpellSystem.getSpellsTriggerManager().action(player, ItemBindTrigger.ATTACK)) {
                event.setCancelled(true);
            }
        }
    }

}
