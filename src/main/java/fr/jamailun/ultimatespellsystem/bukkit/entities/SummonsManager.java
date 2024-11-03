package fr.jamailun.ultimatespellsystem.bukkit.entities;

import fr.jamailun.ultimatespellsystem.bukkit.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.bukkit.events.EntitySummonedEvent;
import fr.jamailun.ultimatespellsystem.bukkit.runner.SpellRuntime;
import fr.jamailun.ultimatespellsystem.bukkit.spells.SpellEntity;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * A registry for all summons. It's a singleton.
 */
public class SummonsManager {

    private final Map<UUID, SummonAttributes> summonedEntities = new HashMap<>();

    /**
     * Summon a creature.
     * @param summon the attributes to use.
     * @return a reference to the newly created entity.
     */
    public @NotNull SpellEntity summon(@NotNull SummonAttributes summon, @NotNull SpellRuntime runtime) {
        // Summon
        summon.summon(this::remove, runtime);
        summonedEntities.put(summon.getUUID(), summon);

        // propagate info
        Bukkit.getPluginManager().callEvent(new EntitySummonedEvent(summon));

        // Return entity
        return summon.getEntity();
    }

    /**
     * Remove a summoned entity.
     * @param uuid the UUID of the creature to remove.
     */
    public void remove(UUID uuid) {
        SummonAttributes removed = summonedEntities.remove(uuid);
        if(removed != null) {
            removed.getKillTask().cancel();
            SpellEntity entity = removed.getEntity();
            if(entity != null)
                entity.remove();
        }
    }

    /**
     * Check if a UUID belongs to a summoned creature.
     * @param uuid the UUID to check.
     * @return true if a summoned creature owns this UUID.
     */
    public boolean isASummonedEntity(@NotNull UUID uuid) {
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
