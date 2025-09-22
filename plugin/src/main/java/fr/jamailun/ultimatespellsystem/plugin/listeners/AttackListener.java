package fr.jamailun.ultimatespellsystem.plugin.listeners;

import fr.jamailun.ultimatespellsystem.api.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.api.bind.ItemBindTrigger;
import fr.jamailun.ultimatespellsystem.api.bind.SpellsTriggerManager.ActionResult;
import fr.jamailun.ultimatespellsystem.plugin.configuration.UssConfig;
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
        if(!(event.getDamager() instanceof Player player))
            return;

        boolean shouldCancel = UltimateSpellSystem.getSpellsTriggerManager().action(player, ItemBindTrigger.ATTACK) != ActionResult.IGNORED;
        if(UssConfig.shouldCancelAttack() && shouldCancel) {
            event.setCancelled(true);
        }
    }

}
