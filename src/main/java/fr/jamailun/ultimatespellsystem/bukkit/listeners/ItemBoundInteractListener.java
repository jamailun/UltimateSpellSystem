package fr.jamailun.ultimatespellsystem.bukkit.listeners;

import fr.jamailun.ultimatespellsystem.bukkit.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.bukkit.bind.ItemBinder;
import fr.jamailun.ultimatespellsystem.bukkit.events.BoundSpellCastEvent;
import fr.jamailun.ultimatespellsystem.bukkit.spells.Spell;
import fr.jamailun.ultimatespellsystem.bukkit.utils.UssConfig;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class ItemBoundInteractListener implements Listener {

    private final ItemBinder binder;
    private final UssConfig config;

    @EventHandler(priority = EventPriority.HIGH)
    void playerInteracts(@NotNull PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if(!config.doesTriggerInteract(event.getAction(), player))
            return;

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
                // Decrement item-count if needed.
                if(player.getGameMode() != GameMode.CREATIVE && UltimateSpellSystem.getItemBinder().hasDestroyKey(inHand)) {
                    player.getInventory().getItemInMainHand().setAmount(inHand.getAmount() - 1);
                }
            }
            event.setUseInteractedBlock(config.isAfterTriggerUseBlock() ? Event.Result.DEFAULT : Event.Result.DENY);
            event.setUseItemInHand(config.isAfterTriggerUseItem() ? Event.Result.DEFAULT : Event.Result.DENY);
        });
    }

}
