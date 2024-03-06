package fr.jamailun.ultimatespellsystem.bukkit.listeners;

import fr.jamailun.ultimatespellsystem.bukkit.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.bukkit.bind.ItemBinder;
import fr.jamailun.ultimatespellsystem.bukkit.events.BoundSpellCast;
import fr.jamailun.ultimatespellsystem.bukkit.spells.SpellDefinition;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class ItemBoundInteractListener implements Listener {

    private final ItemBinder binder;
    public ItemBoundInteractListener(ItemBinder binder) {
        this.binder = binder;
    }

    @EventHandler
    public void playerInteracts(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack inHand = player.getInventory().getItemInMainHand();

        binder.tryFindBoundSpell(inHand).ifPresent(id -> {
            SpellDefinition def = UltimateSpellSystem.getSpellsManager().getSpell(id);
            if(def == null) {
                UltimateSpellSystem.logError("Player " + player.getName() + " used item " + inHand + ". Unknown spell-id: '"+id+"'.");
                return;
            }
            BoundSpellCast cast = new BoundSpellCast(player, def, inHand);
            Bukkit.getPluginManager().callEvent(cast);
            if( ! cast.isCancelled()) {
                // Not cancellable after that !
                def.castNotCancellable(player);
            }
        });
    }

}
