package fr.jamailun.ultimatespellsystem.plugin.listeners;

import fr.jamailun.ultimatespellsystem.api.events.BoundSpellCastEvent;
import fr.jamailun.ultimatespellsystem.api.utils.StringTransformation;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Duration;
import fr.jamailun.ultimatespellsystem.plugin.configuration.UssConfig;
import fr.jamailun.ultimatespellsystem.plugin.spells.SpellsCooldowns;
import lombok.RequiredArgsConstructor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class BoundSpellCastListener implements Listener {

    private final UssConfig config;

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    void onBoundItemCast(@NotNull BoundSpellCastEvent event) {
        Duration cooldown = event.getBindData().getCooldown();
        if(cooldown == null)
            return;

        boolean ignore = event.getCaster() instanceof Player player && player.getGameMode() == GameMode.CREATIVE;
        if(ignore || SpellsCooldowns.canCast(event.getCaster().getUniqueId(), event.getSpellId(), cooldown)) {
            // Send cooldown ?
            if(config.cooldownOnMaterial() && event.getCaster() instanceof Player player && !ignore) {
                player.setCooldown(event.getItem().getType(), (int)cooldown.toTicks());
            }
            return;
        }

        // Cannot cast
        event.setCancelled(true);
        String message = config.messageOnCooldown();
        if(!message.isEmpty())
            event.getCaster().sendMessage(StringTransformation.parse(message));
    }

}
