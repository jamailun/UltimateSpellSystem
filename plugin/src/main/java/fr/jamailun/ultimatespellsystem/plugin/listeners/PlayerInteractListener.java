package fr.jamailun.ultimatespellsystem.plugin.listeners;

import fr.jamailun.ultimatespellsystem.api.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.api.bind.ItemBindTrigger;
import fr.jamailun.ultimatespellsystem.api.bind.SpellsTriggerManager.ActionResult;
import fr.jamailun.ultimatespellsystem.plugin.configuration.UssConfig;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Trigger actions for interactions.
 */
public class PlayerInteractListener implements Listener {

    private final Duration spamDuration = Duration.of(100, ChronoUnit.MILLIS);
    private final Map<UUID, Instant> spamBlocker = new HashMap<>();

    @EventHandler(priority = EventPriority.HIGH)
    void onPlayerInteracts(@NotNull PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if(event.getAction() == Action.PHYSICAL || !canDo(player.getUniqueId()))
            return;

        ActionResult result = UltimateSpellSystem.getSpellsTriggerManager().action(player, convert(event.getAction()));
        boolean shouldCancel = switch (result) {
            case SPELL_CAST -> UssConfig.shouldCancelCast();
            case STEP_VALID -> UssConfig.shouldCancelStep();
            case IGNORED -> false;
        };
        if(shouldCancel) {
            event.setUseInteractedBlock(Event.Result.DENY);
            event.setUseItemInHand(Event.Result.DENY);
            event.setCancelled(true);
        }
    }

    private static @NotNull ItemBindTrigger convert(@NotNull Action action) {
        return switch (action) {
            case LEFT_CLICK_BLOCK -> ItemBindTrigger.LEFT_CLICK_BLOCK;
            case RIGHT_CLICK_BLOCK -> ItemBindTrigger.RIGHT_CLICK_BLOCK;
            case LEFT_CLICK_AIR -> ItemBindTrigger.LEFT_CLICK_AIR;
            case RIGHT_CLICK_AIR -> ItemBindTrigger.RIGHT_CLICK_AIR;
            default -> throw new IllegalStateException("Unexpected value: " + action);
        };
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
