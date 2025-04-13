package fr.jamailun.ultimatespellsystem.plugin.listeners;

import fr.jamailun.ultimatespellsystem.api.bind.ItemBindTrigger;
import fr.jamailun.ultimatespellsystem.plugin.bind.ItemBinderImpl;
import fr.jamailun.ultimatespellsystem.api.events.BoundSpellCastEvent;
import fr.jamailun.ultimatespellsystem.plugin.entities.BukkitSpellEntity;
import fr.jamailun.ultimatespellsystem.plugin.utils.UssConfig;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Listen to attacks and trigger when required.
 */
@RequiredArgsConstructor
public class AttackListener implements Listener {

    private final ItemBinderImpl binder;
    private final UssConfig config;

    @EventHandler(priority = EventPriority.HIGH)
    void playerLeftClick(@NotNull EntityDamageByEntityEvent event) {
        if(event.getDamager() instanceof Mob mob) {
            handle(mob, mob.getEquipment().getItemInMainHand(), event);
        } else if(event.getDamager() instanceof Player player) {
            if(config.doesTriggerAttack(player)) {
                handle(player, player.getEquipment().getItemInMainHand(), event);
            }
        }
    }

    private void handle(@NotNull LivingEntity entity, @NotNull ItemStack item, EntityDamageByEntityEvent event) {
        binder.getBindData(item).ifPresent(data -> {
            BoundSpellCastEvent cast = new BoundSpellCastEvent(entity, data, item, ItemBindTrigger.ATTACK);
            Bukkit.getPluginManager().callEvent(cast);
            if( ! cast.isCancelled()) {
                // Not cancellable after that !
                data.getSpell().castNotCancellable(new BukkitSpellEntity(entity));
                // Don't decrement item.
            }
            event.setCancelled(cast.isInteractionCancelled());
        });

    }

}
