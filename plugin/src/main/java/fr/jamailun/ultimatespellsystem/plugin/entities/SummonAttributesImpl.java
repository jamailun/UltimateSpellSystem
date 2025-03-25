package fr.jamailun.ultimatespellsystem.plugin.entities;

import fr.jamailun.ultimatespellsystem.api.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.api.entities.SummonAttributes;
import fr.jamailun.ultimatespellsystem.api.entities.UssEntityType;
import fr.jamailun.ultimatespellsystem.api.entities.SpellEntity;
import fr.jamailun.ultimatespellsystem.api.events.SummonedEntityExpiredEvent;
import fr.jamailun.ultimatespellsystem.api.runner.SpellRuntime;
import fr.jamailun.ultimatespellsystem.api.providers.SummonPropertiesProvider;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Duration;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.event.Event;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.time.Instant;
import java.util.*;
import java.util.function.Consumer;

/**
 * This is a container-class, for all useful elements about a summoned entity.
 */
public class SummonAttributesImpl implements SummonAttributes {

    protected final SpellEntity summoner;
    protected final Map<String, Object> attributes;
    protected final Location summonLocation;
    protected final UssEntityType summonEntityType;
    protected final Duration summonDuration;
    protected SpellEntity entity;
    protected Instant summonInstant;
    protected BukkitRunnable deathTimer;

    /**
     * Create a new set of attributes. Don't forget to call {@link fr.jamailun.ultimatespellsystem.api.entities.SummonsManager#summon(SummonAttributes, SpellRuntime)} to proceed
     * to the summoning.
     * @param summoner the entity owning the creature.
     * @param location the location on which summon the creature.
     * @param type the USS entity type of creature to summon.
     * @param attributes properties to use. Implementation-dependent.
     * @param duration the duration of the summoning, after which the creature will be removed.
     */
    public SummonAttributesImpl(SpellEntity summoner, Location location, UssEntityType type, Map<String, Object> attributes, Duration duration) {
        this.summoner = summoner;
        this.summonLocation = location;
        this.summonEntityType = type;
        this.summonDuration = duration;
        this.attributes = attributes;
    }

    @Override
    public boolean hasBeenSummoned() {
        return entity != null;
    }

    /**
     * Summon the entity.
     * @param callback the callback to call once the entity is removed.
     */
    public final void summon(Consumer<UUID> callback, SpellRuntime runtime) {
        if(hasBeenSummoned())
            throw new IllegalStateException("Cannot summon an already summoned summon.");

        summonInstant = Instant.now();

        // Summon the entity
        if(summonEntityType.isBukkit()) {
            Entity raw = summonLocation.getWorld().spawnEntity(summonLocation, summonEntityType.getBukkit(), false);
            if(raw instanceof Projectile projectile) {
                // Set the projectile source... if possible
                if(runtime.getCaster().getBukkitEntity().orElse(null) instanceof ProjectileSource ps) {
                    projectile.setShooter(ps);
                }
                // Set the pickup status when required.
                if(projectile instanceof Arrow arrow) {
                    arrow.setPickupStatus(AbstractArrow.PickupStatus.DISALLOWED);
                }
            }
            // Always wrap the entity.
            entity = new BukkitSpellEntity(raw);
        } else {
            entity = summonEntityType.generateCustom(this);
        }

        // Apply properties
        SummonPropertiesProvider.Context context = new SummonPropertiesProvider.Context(runtime, this);
        for(String key : attributes.keySet()) {
            SummonPropertiesProvider.instance()
                    .findOptional(key)
                    .ifPresent(p -> p.accept(entity, attributes.get(key), context));
        }

        // Start the death timer
        deathTimer = UltimateSpellSystem.runTaskLater(() -> {
            if(entity.isValid()) {
                // Called before the remove, so that the entity is still accessible.
                Bukkit.getPluginManager().callEvent(new SummonedEntityExpiredEvent(this));
            }
            entity.remove();
            callback.accept(getUUID());
        }, summonDuration.toTicks());
    }

    @Override
    public @NotNull SpellEntity getSummoner() {
        return summoner;
    }

    @Override
    public SpellEntity getEntity() {
        return entity;
    }

    @Override
    public @NotNull UUID getUUID() {
        return entity.getUniqueId();
    }

    @Override
    public @NotNull Instant getDeathInstant() {
        return summonInstant.plusMillis(summonDuration.toMs());
    }

    @Override
    public @NotNull Instant getSummonInstant() {
        return summonInstant;
    }

    @Override
    public BukkitRunnable getKillTask() {
        return deathTimer;
    }

    @Override
    public @NotNull @UnmodifiableView Map<String, Object> getAttributes() {
        return Collections.unmodifiableMap(attributes);
    }

    @Override
    public @NotNull Location getLocation() {
        return summonLocation;
    }

    @Override
    public @NotNull UssEntityType getEntityType() {
        return summonEntityType;
    }

    @Override
    public @NotNull Duration getDuration() {
        return summonDuration;
    }

    private final Map<Class<? extends Event>, Set<Consumer<? extends Event>>> listeners = new HashMap<>();

    @Override
    public <E extends Event> void registerCallback(@NotNull Class<E> event, @NotNull Consumer<E> callback) {
        listeners.putIfAbsent(event, new HashSet<>());
        listeners.get(event).add(callback);
    }

    @Override
    public void applyCallback(@NotNull Event event) {
        if(!listeners.containsKey(event.getClass()))
            return;
        listeners.get(event.getClass()).forEach(l -> handleEvent(event, l));
    }

    @SuppressWarnings("unchecked")
    private <E extends Event> void handleEvent(Event raw, @NotNull Consumer<E> listener) {
        listener.accept((E) raw);
    }

    @Override
    public @Nullable Object getAttribute(@NotNull String key) {
        return attributes.get(key);
    }

    @Override
    public <T> @Nullable T tryGetAttribute(@NotNull String key, @NotNull Class<T> clazz) {
        Object object = getAttribute(key);
        try {
            return clazz.cast(object);
        } catch(ClassCastException e) {
            assert object != null; // Compiler helper : object cannot be null with this exception.
            UltimateSpellSystem.logWarning("Attribute '" + key +"' in summon was expected of type " + clazz + " but was " + object.getClass() + ".");
            return null;
        }
    }

    @Override
    public <T> @NotNull T tryGetAttribute(@NotNull String key, @NotNull Class<T> clazz, @NotNull T defaultValue) {
        T t = tryGetAttribute(key, clazz);
        return Objects.requireNonNullElse(t, defaultValue);
    }

    @Override
    public boolean hasAttribute(@NotNull String key) {
        return attributes.containsKey(key);
    }

    @Override
    public <R> @NotNull List<R> tryGetAttributes(@NotNull String key, @NotNull Class<R> clazz) {
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
