package fr.jamailun.ultimatespellsystem.bukkit.entities;

import fr.jamailun.ultimatespellsystem.bukkit.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Duration;
import fr.jamailun.ultimatespellsystem.bukkit.extensible.SummonPropertiesExtension;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * This is a container-class, for all useful elements about a summoned entity.
 */
public class SummonAttributes {

    private final Entity summoner;
    private final Map<String, Object> attributes;
    private final Location summonLocation;
    private final EntityType summonEntityType;
    private final Duration summonDuration;
    private Entity entity;
    private Instant summonInstant;
    private BukkitRunnable deathTimer;

    public SummonAttributes(Entity summoner, Location location, EntityType type, Map<String, Object> attributes, Duration duration) {
        this.summoner = summoner;
        this.summonLocation = location;
        this.summonEntityType = type;
        this.summonDuration = duration;
        this.attributes = attributes;
    }

    public boolean hasBeenSummoned() {
        return entity != null;
    }

    /**
     * Summon the entity.
     * @param callback the callback to call once the entity is removed.
     */
    void summon(Consumer<UUID> callback) {
        if(hasBeenSummoned())
            throw new IllegalStateException("Cannot summon an already summoned summon.");

        // Summon the entity
        entity = summonLocation.getWorld().spawnEntity(summonLocation, summonEntityType, false);
        summonInstant = Instant.now();

        // Apply properties
        for(String key : attributes.keySet()) {
            SummonPropertiesExtension.SummonProperty prop = SummonPropertiesExtension.instance().getApplier(key);
            if(prop != null)
                prop.accept(entity, attributes.get(key));
        }

        // Start the death timer
        deathTimer = UltimateSpellSystem.runTaskLater(() -> {
            entity.remove();
            callback.accept(getUUID());
        }, summonDuration.toTicks());
    }

    public boolean isAlive() {
        return entity.isValid() && !entity.isDead();
    }

    /**
     * Get the UUID of the summoned entity.
     * @return a non-null UUID.
     */
    public UUID getUUID() {
        return entity.getUniqueId();
    }

    public Entity getSummoner() {
        return summoner;
    }

    public Entity getEntity() {
        return entity;
    }

    public Instant getDeathInstant() {
        return summonInstant.plusMillis(summonDuration.toMs());
    }

    public Instant getSummonInstant() {
        return summonInstant;
    }

    public BukkitRunnable getKillTask() {
        return deathTimer;
    }

    public Map<String, Object> getAttributes() {
        return Map.copyOf(attributes);
    }

    public @Nullable Object getAttribute(String key) {
        return attributes.get(key);
    }

    public Location getSummonLocation() {
        return summonLocation;
    }

    public EntityType getSummonEntityType() {
        return summonEntityType;
    }

    public Duration getSummonDuration() {
        return summonDuration;
    }
}
