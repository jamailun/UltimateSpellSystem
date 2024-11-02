package fr.jamailun.ultimatespellsystem.bukkit.listeners;

import fr.jamailun.ultimatespellsystem.bukkit.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.bukkit.bind.ItemBinder;
import fr.jamailun.ultimatespellsystem.bukkit.events.BoundSpellCastEvent;
import fr.jamailun.ultimatespellsystem.bukkit.spells.Spell;
import org.bukkit.Bukkit;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class AttackListener implements Listener {

    private final ItemBinder binder;

    public AttackListener(ItemBinder binder) {
        this.binder = binder;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void playerLeftClick(EntityDamageByEntityEvent event) {
        if(event.getDamager() instanceof Mob mob) {
            handle(mob, mob.getEquipment().getItemInMainHand(), event);
        } else if(event.getDamager() instanceof HumanEntity human) {
            handle(human, human.getEquipment().getItemInMainHand(), event);
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
