package fr.jamailun.ultimatespellsystem.plugin.entities;

import fr.jamailun.ultimatespellsystem.api.entities.SummonAttributes;
import fr.jamailun.ultimatespellsystem.api.entities.SummonsManager;
import fr.jamailun.ultimatespellsystem.api.events.EntitySummonedEvent;
import fr.jamailun.ultimatespellsystem.plugin.listeners.AggroListener;
import fr.jamailun.ultimatespellsystem.api.runner.SpellRuntime;
import fr.jamailun.ultimatespellsystem.api.entities.SpellEntity;
import fr.jamailun.ultimatespellsystem.plugin.utils.Clock;
import fr.jamailun.ultimatespellsystem.plugin.configuration.UssConfig;
import org.bukkit.Bukkit;
import org.bukkit.entity.Mob;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * A registry for all summons. It's a singleton.
 */
public class SummonsManagerImpl implements SummonsManager {

    private final Map<UUID, SummonAttributes> summonedEntities = new HashMap<>();
    private final Clock aggroClock;

    public SummonsManagerImpl(@NotNull UssConfig config) {
        config.registerObserver(this::refreshConfig);
        aggroClock = new Clock(this::recomputeAggro, UssConfig.getTicksAggroSummons());
    }

    private void recomputeAggro() {
        for(SummonAttributes summon : summonedEntities.values()) {
            if(summon.getEntity().getBukkitEntity().orElse(null) instanceof Mob mob && (mob.getTarget() == null || !mob.getTarget().isValid())) {
                mob.setTarget(AggroListener.findAggro(summon));
            }
        }
    }

    @Override
    public @NotNull SpellEntity summon(@NotNull SummonAttributes summon, @NotNull SpellRuntime runtime) {
        // Summon
        summon.summon(this::remove, runtime);
        summonedEntities.put(summon.getUUID(), summon);

        // propagate info
        Bukkit.getPluginManager().callEvent(new EntitySummonedEvent(summon));

        // Return entity
        return summon.getEntity();
    }

    @Override
    public void remove(@NotNull UUID uuid) {
        SummonAttributes removed = summonedEntities.remove(uuid);
        if(removed != null) {
            removed.getKillTask().cancel();
            SpellEntity entity = removed.getEntity();
            if(entity != null)
                entity.remove();
        }
    }

    @Override
    public boolean isASummonedEntity(@NotNull UUID uuid) {
        return summonedEntities.containsKey(uuid);
    }

    @Override
    public @Nullable UUID getUuidOfSummoner(@NotNull UUID uuid) {
        return Optional.ofNullable(find(uuid))
                .map(a -> a.getSummoner().getUniqueId())
                .orElse(null);
    }

    @Override
    public @Nullable SummonAttributes find(@NotNull UUID uuid) {
        return summonedEntities.get(uuid);
    }

    public void refreshConfig(@NotNull UssConfig config) {
        aggroClock.setFrequency(UssConfig.getTicksAggroSummons());
    }

    @Override
    public int purgeAll() {
        int size = summonedEntities.size();
        for(UUID uuid : List.copyOf(summonedEntities.keySet())) {
            var summon = summonedEntities.get(uuid);
            // Clean
            summon.getKillTask().cancel();
            summonedEntities.remove(uuid);
            // Remove from world
            summon.getEntity().remove();
        }
        return size;
    }

}
