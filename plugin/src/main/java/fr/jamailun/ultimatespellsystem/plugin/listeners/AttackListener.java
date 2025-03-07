package fr.jamailun.ultimatespellsystem.plugin.listeners;

import fr.jamailun.ultimatespellsystem.api.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.api.spells.Spell;
import fr.jamailun.ultimatespellsystem.plugin.bind.ItemBinderImpl;
import fr.jamailun.ultimatespellsystem.api.events.BoundSpellCastEvent;
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
        binder.tryFindBoundSpell(item).ifPresent(id -> {
            Spell def = UltimateSpellSystem.getSpellsManager().getSpell(id);
            if(def == null) {
                UltimateSpellSystem.logError("Entity " + entity.getName() + " used item " + item + ". Unknown spell-id: '"+id+"'.");
                return;
            }
            BoundSpellCastEvent cast = new BoundSpellCastEvent(entity, def, item, BoundSpellCastEvent.Action.ATTACK);
            Bukkit.getPluginManager().callEvent(cast);
            if( ! cast.isCancelled()) {
                // Not cancellable after that !
                def.castNotCancellable(entity);
                // Don't decrement item.
            }
            event.setCancelled(cast.isInteractionCancelled());
        });

    }

}
