package fr.jamailun.ultimatespellsystem.api.bukkit.entities;

import fr.jamailun.ultimatespellsystem.api.bukkit.runner.SpellRuntime;
import fr.jamailun.ultimatespellsystem.bukkit.entities.SummonAttributesImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * A registry for all summons. It's a singleton.
 */
public interface SummonsManager {

    /**
     * Summon a creature.
     * @param summon the attributes to use.
     * @return a reference to the newly created entity.
     */
    @NotNull SpellEntity summon(@NotNull SummonAttributesImpl summon, @NotNull SpellRuntime runtime);

    /**
     * Remove a summoned entity.
     * @param uuid the UUID of the creature to remove.
     */
    void remove(@NotNull UUID uuid);

    /**
     * Check if a UUID belongs to a summoned creature.
     * @param uuid the UUID to check.
     * @return true if a summoned creature owns this UUID.
     */
    boolean isASummonedEntity(@NotNull UUID uuid);

    /**
     * Get the UUID of the summoner, if the UUID belongs tpo a summoned entity.
     * @param uuid the UUID to test
     * @return the UUID of the summoner, if the parameter UUID belongs to a summoned entity.
     */
    @Nullable UUID getUuidOfSummoner(@NotNull UUID uuid);

    /**
     * Try to find an entity.
     * @param uuid the UUID of the entity to look for.
     * @return an empty Optional if no summoned entity has the requested UUID.
     */
    @Nullable SummonAttributesImpl find(@NotNull UUID uuid);

    /**
     * Purge all summons.
     * @return the amount of purged summons.
     */
    int purgeAll();
}
