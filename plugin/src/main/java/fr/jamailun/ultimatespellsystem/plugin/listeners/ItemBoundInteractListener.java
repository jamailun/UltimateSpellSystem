package fr.jamailun.ultimatespellsystem.plugin.listeners;

import fr.jamailun.ultimatespellsystem.api.bind.ItemBindTrigger;
import fr.jamailun.ultimatespellsystem.api.bind.SpellBindData;
import fr.jamailun.ultimatespellsystem.api.entities.SpellEntity;
import fr.jamailun.ultimatespellsystem.plugin.bind.ItemBinderImpl;
import fr.jamailun.ultimatespellsystem.api.events.BoundSpellCastEvent;
import fr.jamailun.ultimatespellsystem.plugin.entities.BukkitSpellEntity;
import fr.jamailun.ultimatespellsystem.plugin.utils.UssConfig;
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
    void onPlayerInteracts(@NotNull PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if(!config.doesTriggerInteract(event.getAction(), player) || !canDo(player.getUniqueId()))
            return;
        ItemBindTrigger trigger = ItemBindTrigger.convert(event.getAction());
        if(trigger == null)
            return; // ignored

        ItemStack inHand = player.getInventory().getItemInMainHand();

        // If it finds the spell, cast it (according to event result)
        binder.getBindDatas(inHand).ifPresent(list -> {
            //TODO handle multiple spells...
            cast(player, list.getFirst(), inHand, event, trigger);
        });
    }

    private void cast(@NotNull Player player, @NotNull SpellBindData data, @NotNull ItemStack item, @NotNull PlayerInteractEvent event, @NotNull ItemBindTrigger trigger) {
        SpellEntity caster = new BukkitSpellEntity(player);
        boolean bypass = player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR;
        // can afford ?
        if(!bypass && !data.getCost().canPay(caster)) {
            // Cannot pay !
            //TODO faire ça
            return;
        }

        // Send event
        BoundSpellCastEvent cast = new BoundSpellCastEvent(player, data, item, trigger);
        Bukkit.getPluginManager().callEvent(cast);

        // Always negate world effect
        event.setUseInteractedBlock(config.isAfterTriggerUseBlock() ? Event.Result.DEFAULT : Event.Result.DENY);
        event.setUseItemInHand(config.isAfterTriggerUseItem() ? Event.Result.DEFAULT : Event.Result.DENY);

        // If cancelled, do nothing
        if(cast.isCancelled())
            return;

        // Not cancellable after that !
        boolean success = data.getSpell().castNotCancellable(caster);

        // Pay
        if(success && !bypass) {
            data.getCost().pay(caster);
        }
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
