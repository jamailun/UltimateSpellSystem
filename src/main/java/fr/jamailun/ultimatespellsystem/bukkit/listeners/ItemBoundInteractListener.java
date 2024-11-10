package fr.jamailun.ultimatespellsystem.bukkit.listeners;

import fr.jamailun.ultimatespellsystem.api.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.api.bukkit.spells.Spell;
import fr.jamailun.ultimatespellsystem.bukkit.bind.ItemBinderImpl;
import fr.jamailun.ultimatespellsystem.api.bukkit.events.BoundSpellCastEvent;
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

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
public class ItemBoundInteractListener implements Listener {

    private final ItemBinderImpl binder;
    private final UssConfig config;

    private final Duration spamDuration = Duration.of(100, ChronoUnit.MILLIS);
    private final Map<UUID, Instant> spamBlocker = new HashMap<>();

    @EventHandler(priority = EventPriority.HIGH)
    void playerInteracts(@NotNull PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if(!config.doesTriggerInteract(event.getAction(), player) || !canDo(player.getUniqueId()))
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
                boolean success = def.castNotCancellable(player);
                // Decrement item-count if needed.
                if(success && player.getGameMode() != GameMode.CREATIVE && UltimateSpellSystem.getItemBinder().hasDestroyKey(inHand)) {
                    player.getInventory().getItemInMainHand().setAmount(inHand.getAmount() - 1);
                }
            }
            event.setUseInteractedBlock(config.isAfterTriggerUseBlock() ? Event.Result.DEFAULT : Event.Result.DENY);
            event.setUseItemInHand(config.isAfterTriggerUseItem() ? Event.Result.DEFAULT : Event.Result.DENY);
        });
    }

    private boolean canDo(@NotNull UUID uuid) {
        Instant allowed = spamBlocker.get(uuid);
        Instant now = Instant.now();
        if(allowed == null || now.isAfter(allowed)) {
            spamBlocker.put(uuid, now.plus(spamDuration));
            return true;
        }
        return false;
    }

}
