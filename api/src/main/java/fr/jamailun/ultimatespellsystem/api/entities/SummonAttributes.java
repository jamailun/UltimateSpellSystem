package fr.jamailun.ultimatespellsystem.api.entities;

import fr.jamailun.ultimatespellsystem.api.runner.SpellRuntime;
import fr.jamailun.ultimatespellsystem.api.utils.AttributesHolder;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Duration;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.time.Instant;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;

/**
 * This is a container-like, for all useful elements about a summoned entity.
 */
public interface SummonAttributes extends AttributesHolder {

    @ApiStatus.Internal
    void summon(Consumer<UUID> callback, SpellRuntime runtime);

    /**
     * Check if the summoning has been done.
     * @return true if all methods are safe.
     */
    @Contract(pure = true)
    boolean hasBeenSummoned();

    /**
     * Get the summoner, i.e. the caster.
     * @return a non-null LivingEntity.
     */
    @Contract(pure = true)
    @NotNull LivingEntity getSummoner();

    /**
     * Get the summoned entity.
     * @return an entity. Can be null if the summoning has not been done.
     */
    @Contract(pure = true)
    SpellEntity getEntity();

    /**
     * Get the UUID of the summoned entity.
     * @return an UUID.
     * @throws NullPointerException if the summoning has not been done.
     */
    @NotNull UUID getUUID();

    /**
     * Get the instant the summoning operation was done.
     * @return a non-null instant.
     */
    @NotNull Instant getDeathInstant();

    /**
     * Get the instant the summoned creature will be removed.
     * @return a non-null instant.
     */
    @NotNull Instant getSummonInstant();

    /**
     * Get the task that will remove the entity.
     * @return a task.
     */
    BukkitRunnable getKillTask();

    /**
     * Get the full map of attributes.
     * @return an unmodifiable view of the attributes map.
     */
    @NotNull @UnmodifiableView Map<String, Object> getAttributes();

    /**
     * Get the location of the summoned creature on spawn.
     * @return a non-null Bukkit location.
     */
    @NotNull Location getLocation();

    /**
     * Get the summoned entity type.
     * @return a non-null USS entity type.
     */
    @NotNull UssEntityType getEntityType();

    /**
     * Get the duration of the summoned creature.
     * @return a duration, after which the summoned creature will be removed.
     */
    @NotNull Duration getDuration();

    /**
     * Register a new callback.
     * @param event the event class.
     * @param callback a runnable callback. First argument is this {@link SummonAttributes} instance. Second argument is the event itself.
     * @param <E> generic of the event.
     */
    <E extends Event> void registerCallback(@NotNull Class<E> event, @NotNull Consumer<E> callback);

    /**
     * Propagate an event. May do nothing : the implementation decides what to do.
     * @param event the non-nul bukkit event.
     * @see #registerCallback(Class, Consumer)
     */
    void applyCallback(@NotNull Event event);

}
