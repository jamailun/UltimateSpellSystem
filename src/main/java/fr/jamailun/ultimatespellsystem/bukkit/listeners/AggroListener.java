package fr.jamailun.ultimatespellsystem.bukkit.listeners;

import fr.jamailun.ultimatespellsystem.bukkit.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.bukkit.entities.SummonAttributes;
import fr.jamailun.ultimatespellsystem.bukkit.utils.EntitiesFinder;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class AggroListener implements Listener {

    @EventHandler
    void aggroChanged(@NotNull EntityTargetLivingEntityEvent event) {
        Optional<SummonAttributes> attrOpt = UltimateSpellSystem.getSummonsManager().find(event.getEntity().getUniqueId());
        if(attrOpt.isEmpty())
            return;
        SummonAttributes summon = attrOpt.get();

        // has target ?
        if(event.getTarget() == null) {
            // Always find closest
            event.setTarget(findAggro(summon));
        } else {
            if(canAggro(summon, event.getTarget()))
                return;

            // If we can, change the target
            Entity newTarget = findAggro(summon);
            if(newTarget == null) {
                // We cancel the event if nothing changed
                event.setCancelled(true);
            } else {
                event.setTarget(newTarget);
            }
        }
    }

    private static boolean canAggro(@NotNull SummonAttributes summon, @NotNull Entity target) {
        // Ignore invulnerable entities
        if(target instanceof LivingEntity living) {
            if(living.isInvulnerable())
                return false;
        }
        // Ignore players in CREATIVE and in SPECTATOR
        if(target instanceof Player player) {
            if(player.getGameMode() == GameMode.SPECTATOR || player.getGameMode() == GameMode.CREATIVE)
                return false;
        }

        UUID summonerUuid = summon.getSummoner().getUniqueId();

        // Test is caster
        if(Objects.equals(summonerUuid, target.getUniqueId())) {
            return summon.tryGetAttribute("can_aggro_caster", Boolean.class, false);
        }

        // Test is a summon of the same caster
        if(Objects.equals(summonerUuid, UltimateSpellSystem.getSummonsManager().getUuidOfSummoner(target.getUniqueId()))) {
            return summon.tryGetAttribute("can_aggro_summons", Boolean.class, false);
        }

        return true;
    }

    /**
     * Find an aggro-able target for a summon. Beware, his operation can have a certain cost to it.
     * @param summon the summon to check.
     * @return {@code null} if no suitable target has been found.
     */
    public static @Nullable LivingEntity findAggro(@NotNull SummonAttributes summon) {
        Object scope = summon.getAttribute("aggro_scope");
        if(scope == null) return null;

        Location location = summon.getEntity().getLocation();
        double range = summon.tryGetAttribute("aggro_range", Double.class, 7d);
        return EntitiesFinder.findEntitiesAround(scope, location, range)
                .stream()
                // Remove itself
                .filter(e -> !Objects.equals(e.getUniqueId(), summon.getUUID()))
                // Only living
                .filter(e -> e instanceof LivingEntity)
                .map(e -> (LivingEntity) e)
                // Only aggro-able
                .filter(e -> canAggro(summon, e))
                // Get the closest
                .min(Comparator.comparing(e -> e.getLocation().distanceSquared(location)))
                .orElse(null);
    }

}
