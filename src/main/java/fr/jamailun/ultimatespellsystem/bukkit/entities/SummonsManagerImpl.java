package fr.jamailun.ultimatespellsystem.bukkit.entities;

import fr.jamailun.ultimatespellsystem.api.bukkit.entities.SummonsManager;
import fr.jamailun.ultimatespellsystem.api.bukkit.events.EntitySummonedEvent;
import fr.jamailun.ultimatespellsystem.bukkit.listeners.AggroListener;
import fr.jamailun.ultimatespellsystem.api.bukkit.runner.SpellRuntime;
import fr.jamailun.ultimatespellsystem.api.bukkit.entities.SpellEntity;
import fr.jamailun.ultimatespellsystem.bukkit.utils.Clock;
import fr.jamailun.ultimatespellsystem.bukkit.utils.UssConfig;
import org.bukkit.Bukkit;
import org.bukkit.entity.Mob;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * A registry for all summons. It's a singleton.
 */
public class SummonsManagerImpl implements SummonsManager {

    private final Map<UUID, SummonAttributesImpl> summonedEntities = new HashMap<>();
    private final Clock aggroClock;

    public SummonsManagerImpl(@NotNull UssConfig config) {
        config.registerObserver(this::refreshConfig);
        aggroClock = new Clock(this::recomputeAggro, config.getCheckSummonsAggroEverySeconds());
    }

    private void recomputeAggro() {
        for(SummonAttributesImpl summon : summonedEntities.values()) {
            if(summon.getEntity().getBukkitEntity().orElse(null) instanceof Mob mob && (mob.getTarget() == null || !mob.getTarget().isValid())) {
                mob.setTarget(AggroListener.findAggro(summon));
            }
        }
    }

    @Override
    public @NotNull SpellEntity summon(@NotNull SummonAttributesImpl summon, @NotNull SpellRuntime runtime) {
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
        SummonAttributesImpl removed = summonedEntities.remove(uuid);
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
    public @Nullable SummonAttributesImpl find(@NotNull UUID uuid) {
        return summonedEntities.get(uuid);
    }

    public void refreshConfig(@NotNull UssConfig config) {
        aggroClock.setFrequency(config.getCheckSummonsAggroEverySeconds());
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
