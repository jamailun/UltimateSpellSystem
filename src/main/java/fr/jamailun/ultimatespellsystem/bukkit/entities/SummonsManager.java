package fr.jamailun.ultimatespellsystem.bukkit.entities;

import fr.jamailun.ultimatespellsystem.bukkit.events.EntitySummonedEvent;
import fr.jamailun.ultimatespellsystem.bukkit.spells.SpellEntity;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Duration;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.util.*;

/**
 * A registry for all summons. It's a singleton.
 */
public class SummonsManager {

    private final Map<UUID, SummonAttributes> summonedEntities = new HashMap<>();

    public SpellEntity summon(SummonAttributes summon) {
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
            SpellEntity entity = removed.getEntity();
            if(entity != null)
                entity.remove();
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
