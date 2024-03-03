package fr.jamailun.ultimatespellsystem.bukkit.entities;

import fr.jamailun.ultimatespellsystem.bukkit.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.bukkit.extensible.SummonPropertiesExtension;
import fr.jamailun.ultimatespellsystem.bukkit.spells.BukkitSpellEntity;
import fr.jamailun.ultimatespellsystem.bukkit.spells.SpellEntity;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Duration;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * This is a container-class, for all useful elements about a summoned entity.
 */
public class SummonAttributes {

    protected final Entity summoner;
    protected final Map<String, Object> attributes;
    protected final Location summonLocation;
    protected final UssEntityType summonEntityType;
    protected final Duration summonDuration;
    protected SpellEntity entity;
    protected Instant summonInstant;
    protected BukkitRunnable deathTimer;

    public SummonAttributes(Entity summoner, Location location, UssEntityType type, Map<String, Object> attributes, Duration duration) {
        this.summoner = summoner;
        this.summonLocation = location;
        this.summonEntityType = type;
        this.summonDuration = duration;
        this.attributes = attributes;
    }

    public boolean hasBeenSummoned() {
        return entity != null;
    }

    public UUID getUUID() {
        return entity.getUniqueId();
    }

    /**
     * Summon the entity.
     * @param callback the callback to call once the entity is removed.
     */
    final void summon(Consumer<UUID> callback) {
        if(hasBeenSummoned())
            throw new IllegalStateException("Cannot summon an already summoned summon.");

        summonInstant = Instant.now();

        // Summon the entity
        if(summonEntityType.isBukkit()) {
            Entity raw = summonLocation.getWorld().spawnEntity(summonLocation, summonEntityType.getBukkit(), false);
            entity = new BukkitSpellEntity(raw);
        } else {
            entity = summonEntityType.generateCustom(this);
        }

        // Apply properties
        for(String key : attributes.keySet()) {
            SummonPropertiesExtension.instance()
                    .findOptional(key)
                    .ifPresent(p -> p.accept(entity, attributes.get(key)));
        }

        // Start the death timer
        deathTimer = UltimateSpellSystem.runTaskLater(() -> {
            entity.remove();
            callback.accept(getUUID());
        }, summonDuration.toTicks());
    }

    public @NotNull Entity getSummoner() {
        return summoner;
    }

    public SpellEntity getEntity() {
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

    public @NotNull Map<String, Object> getAttributes() {
        return Map.copyOf(attributes);
    }

    public @NotNull Location getLocation() {
        return summonLocation;
    }

    public @NotNull UssEntityType getEntityType() {
        return summonEntityType;
    }

    public @NotNull Duration getDuration() {
        return summonDuration;
    }

    /**
     * Get a specific attribute.
     * @param key the key of the attribute.
     * @return null if the key is not set.
     */
    public @Nullable Object getAttribute(String key) {
        return getAttributes().get(key);
    }

    /**
     * Try to get a custom attribute.
     * @param key the key of the attribute.
     * @param clazz the class to cast the attribute to.
     * @param defaultValue the default value. Returned if the key is not set, <b>or</b> if the cast fails.
     * @return the default value OR a non-null value of required type.
     * @param <R> the generic to, for the returned value.
     */
    public <R> R tryGetAttribute(String key, Class<R> clazz, R defaultValue) {
        Object value = getAttribute(key);
        if(value == null) {
            UltimateSpellSystem.logDebug("tryGetAttribute("+key+"): NULL Entries are " + List.copyOf(getAttributes().keySet()));
            return defaultValue;
        }
        try {
            return clazz.cast(value);
        } catch(ClassCastException e) {
            UltimateSpellSystem.logWarning("Summon tried to read attribute " + key + ": " + e.getMessage());
            return defaultValue;
        }
    }

}
