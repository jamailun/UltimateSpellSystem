package fr.jamailun.ultimatespellsystem.bukkit.listeners;

import fr.jamailun.ultimatespellsystem.bukkit.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.bukkit.bind.ItemBinder;
import fr.jamailun.ultimatespellsystem.bukkit.events.BoundSpellCastEvent;
import fr.jamailun.ultimatespellsystem.bukkit.spells.Spell;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class ItemBoundInteractListener implements Listener {

    private final ItemBinder binder;
    private final boolean onlyRightClick;

    public ItemBoundInteractListener(ItemBinder binder, boolean onlyRightClick) {
        this.binder = binder;
        this.onlyRightClick = onlyRightClick;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void playerInteracts(PlayerInteractEvent event) {
        // Optionally ignore non right-click (but always ignore PHYSICAL !)
        if(event.getAction() == Action.PHYSICAL || onlyRightClick && (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK))
            return;

        Player player = event.getPlayer();
        ItemStack inHand = player.getInventory().getItemInMainHand();

        // If it finds the spell, cast it (according to event result)
        binder.tryFindBoundSpell(inHand).ifPresent(id -> {
            Spell def = UltimateSpellSystem.getSpellsManager().getSpell(id);
            if(def == null) {
                UltimateSpellSystem.logError("Player " + player.getName() + " used item " + inHand + ". Unknown spell-id: '"+id+"'.");
                return;
            }
            BoundSpellCastEvent cast = new BoundSpellCastEvent(player, def, inHand, BoundSpellCastEvent.Action.convert(event.getAction()));
            Bukkit.getPluginManager().callEvent(cast);
            if( ! cast.isCancelled()) {
                // Not cancellable after that !
                def.castNotCancellable(player);
            }
            event.setCancelled(cast.isInteractionCancelled());
        });
    }

}
