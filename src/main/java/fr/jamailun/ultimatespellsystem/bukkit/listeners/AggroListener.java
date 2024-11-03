package fr.jamailun.ultimatespellsystem.bukkit.listeners;

import fr.jamailun.ultimatespellsystem.bukkit.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.bukkit.entities.SummonAttributes;
import fr.jamailun.ultimatespellsystem.bukkit.utils.EntitiesFinder;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;

public class AggroListener implements Listener {

    @EventHandler
    void aggroChanged(@NotNull EntityTargetLivingEntityEvent event) {
        Optional<SummonAttributes> attrOpt = UltimateSpellSystem.getSummonsManager().find(event.getEntity().getUniqueId());
        if(attrOpt.isEmpty())
            return;
        SummonAttributes summon = attrOpt.get();

        boolean canAggro = summon.tryGetAttribute("can_aggro_caster", Boolean.class, false);
        if(Objects.equals(summon.getSummoner(), event.getTarget())) {
            if(!canAggro) {
                LivingEntity newTarget = findAggro(summon);
                if(newTarget == null){
                    event.setCancelled(true);
                } else {
                    event.setTarget(newTarget);
                }
            }
        }
    }

    private @Nullable LivingEntity findAggro(@NotNull SummonAttributes summon) {
        Object scope = summon.getAttribute("aggro_scope");
        if(scope == null) return null;

        Location location = summon.getEntity().getLocation();
        double range = summon.tryGetAttribute("aggro_range", Double.class, 7d);
        return EntitiesFinder.findEntitiesAround(scope, location, range)
                .stream()
                .filter(e -> e instanceof LivingEntity)
                .map(e -> (LivingEntity) e)
                .filter(e -> !Objects.equals(e.getUniqueId(), summon.getUUID()))
                .min(Comparator.comparing(e -> e.getLocation().distanceSquared(location)))
                .orElse(null);
    }

}
