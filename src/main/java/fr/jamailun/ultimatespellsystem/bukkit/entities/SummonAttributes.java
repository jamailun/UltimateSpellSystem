package fr.jamailun.ultimatespellsystem.bukkit.entities;

import fr.jamailun.ultimatespellsystem.bukkit.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.bukkit.providers.SummonPropertiesProvider;
import fr.jamailun.ultimatespellsystem.bukkit.spells.BukkitSpellEntity;
import fr.jamailun.ultimatespellsystem.bukkit.spells.SpellEntity;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Duration;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.*;
import java.util.function.Consumer;

/**
 * This is a container-class, for all useful elements about a summoned entity.
 */
public class SummonAttributes {

    protected final LivingEntity summoner;
    protected final Map<String, Object> attributes;
    protected final Location summonLocation;
    protected final UssEntityType summonEntityType;
    protected final Duration summonDuration;
    protected SpellEntity entity;
    protected Instant summonInstant;
    protected BukkitRunnable deathTimer;

    /**
     * Create a new set of attributes. Don't forget to call {@link SummonsManager#summon(SummonAttributes)} to proceed
     * to the summoning.
     * @param summoner the entity owning the creature.
     * @param location the location on which summon the creature.
     * @param type the USS entity type of creature to summon.
     * @param attributes properties to use. Implementation-dependent.
     * @param duration the duration of the summoning, after which the creature will be removed.
     */
    public SummonAttributes(LivingEntity summoner, Location location, UssEntityType type, Map<String, Object> attributes, Duration duration) {
        this.summoner = summoner;
        this.summonLocation = location;
        this.summonEntityType = type;
        this.summonDuration = duration;
        this.attributes = attributes;
    }

    /**
     * Check if the summoning has been done.
     * @return true if all methods are safe.
     */
    public boolean hasBeenSummoned() {
        return entity != null;
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
            SummonPropertiesProvider.instance()
                    .findOptional(key)
                    .ifPresent(p -> p.accept(entity, attributes.get(key)));
        }

        // Start the death timer
        deathTimer = UltimateSpellSystem.runTaskLater(() -> {
            entity.remove();
            callback.accept(getUUID());
        }, summonDuration.toTicks());
    }

    /**
     * Get the summoner, i.e. the caster.
     * @return a non-null LivingEntity.
     */
    public @NotNull LivingEntity getSummoner() {
        return summoner;
    }

    /**
     * Get the summoned entity.
     * @return an entity. Can be null if the summoning has not been done.
     */
    public SpellEntity getEntity() {
        return entity;
    }

    /**
     * Get the UUID of the summoned entity.
     * @return an UUID.
     * @throws NullPointerException if the summoning has not been done.
     */
    public @NotNull UUID getUUID() {
        return entity.getUniqueId();
    }

    /**
     * Get the instant the summoning operation was done.
     * @return a non-null instant.
     */
    public @NotNull Instant getDeathInstant() {
        return summonInstant.plusMillis(summonDuration.toMs());
    }

    /**
     * Get the instant the summoned creature will be removed.
     * @return a non-null instant.
     */
    public @NotNull Instant getSummonInstant() {
        return summonInstant;
    }

    /**
     * Get the task that will remove the entity.
     * @return a task.
     */
    public BukkitRunnable getKillTask() {
        return deathTimer;
    }

    /**
     * Get a copy of the map of attributes.
     * @return a non-null and immuable map.
     */
    public @NotNull Map<String, Object> getAttributes() {
        return Map.copyOf(attributes);
    }

    /**
     * Get the location of the summoned creature on spawn.
     * @return a non-null Bukkit location.
     */
    public @NotNull Location getLocation() {
        return summonLocation;
    }

    /**
     * Get the summoned entity type.
     * @return a non-null USS entity type.
     */
    public @NotNull UssEntityType getEntityType() {
        return summonEntityType;
    }

    /**
     * Get the duration of the summoned creature.
     * @return a duration, after wich the summoned creature will be removed.
     */
    public @NotNull Duration getDuration() {
        return summonDuration;
    }

    /**
     * Get a specific attribute.
     * @param key the key of the attribute.
     * @return null if the key is not set.
     */
    public @Nullable Object getAttribute(String key) {
        return attributes.get(key);
    }

    /**
     * Test if an attribute exists.
     * @param key the key to test.
     * @return true if some attribute exists with this key.
     */
    public boolean hasAttribute(String key) {
        return attributes.containsKey(key);
    }

    /**
     * Try to get a custom attribute.
     * @param key the key of the attribute.
     * @param clazz the class to cast the attribute to.
     * @param defaultValue the default value. Returned if the key is not set, <b>or</b> if the cast fails.
     * @return the default value OR a non-null value of required type.
     * @param <R> the generic to use, for the returned value.
     */
    public <R> R tryGetAttribute(String key, Class<R> clazz, R defaultValue) {
        Object value = getAttribute(key);
        if(value == null) {
            UltimateSpellSystem.logDebug("tryGetAttribute("+key+"): NULL Entries are " + List.copyOf(attributes.keySet()));
            return defaultValue;
        }
        try {
            return clazz.cast(value);
        } catch(ClassCastException e) {
            UltimateSpellSystem.logWarning("Summon tried to read attribute " + key + ": " + e.getMessage());
            return defaultValue;
        }
    }

    /**
     * Try to get a custom attribute, as a list.
     * @param key the key of the attribute.
     * @param clazz the class to cast the attribute to.
     * @return a list of attributes, <b>or</b> an empty-list if attribute is ot-found or if an error occurs.
     * @param <R> the generic to use, for the returned value.
     */
    public <R> @NotNull List<R> tryGetAttributes(String key, Class<R> clazz) {
        Object value = getAttribute(key);
        if(!(value instanceof Collection<?> collection)) {
            return Collections.emptyList();
        }
        List<R> list = new ArrayList<>();
        try {
            for(Object item : collection) {
                list.add(clazz.cast(item));
            }
            return list;
        } catch(ClassCastException e) {
            UltimateSpellSystem.logWarning("Summon tried to read attribute " + key + ": " + e.getMessage());
            return Collections.emptyList();
        }
    }

}
