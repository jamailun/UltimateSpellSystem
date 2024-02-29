package fr.jamailun.ultimatespellsystem.entities;

import fr.jamailun.ultimatespellsystem.events.EntitySummonedEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import java.util.*;

/**
 * A registry for all summons. It's a singleton.
 */
public final class SummonsRegistry {
    private static final SummonsRegistry INSTANCE = new SummonsRegistry();
    public static SummonsRegistry getInstance() {return INSTANCE;}
    private SummonsRegistry() {}

    private final Map<UUID, SummonAttributes> summonedEntities = new HashMap<>();

    public Entity summon(SummonAttributes summon) {
        // Summon
        summon.summon(this::remove);
        summonedEntities.put(summon.getUUID(), summon);

        // propagate info
        Bukkit.getPluginManager().callEvent(new EntitySummonedEvent(summon));

        // Return entity
        return summon.getEntity();
    }

    public void remove(UUID uuid) {
        SummonAttributes removed = summonedEntities.remove(uuid);
        if(removed != null) {
            removed.getKillTask().cancel();
            Entity summon = removed.getEntity();
            if(summon != null && summon.isValid())
                summon.remove();
        }
    }

    public boolean isASummonedEntity(UUID uuid) {
        return summonedEntities.containsKey(uuid);
    }

    /**
     * Try to find an entity.
     * @param uuid the UUID of the entity to look for.
     * @return an empty Optional if no summoned entity has the requested UUID.
     */
    public Optional<SummonAttributes> find(UUID uuid) {
        return Optional.ofNullable(summonedEntities.get(uuid));
    }

    /**
     * Purge all summoned entities.
     * @return the amount of removed entities.
     */
    public int purgeAll() {
        int size = summonedEntities.size();
        for(UUID uuid : List.copyOf(summonedEntities.keySet())) {
            summonedEntities.get(uuid).getKillTask().cancel();
            summonedEntities.remove(uuid);
        }
        return size;
    }


}
