package fr.jamailun.ultimatespellsystem.api.entities;

import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

/**
 * An entity-representation inside a {@link fr.jamailun.ultimatespellsystem.api.spells.Spell} logic.
 */
public interface SpellEntity {

    /**
     * Get the UUID of the entity.
     * @return a non-null, unique UUID.
     */
    @NotNull UUID getUniqueId();

    /**
     * Get the bukkit entity, if it exists.
     * @return a Bukkit entity if this is possible.
     */
    @Contract(pure = true)
    @NotNull Optional<Entity> getBukkitEntity();

    /**
     * Test if the current entity has a bukkit representation.
     * @return true if a call to {@link #getBukkitEntity()} returns something.
     */
    default boolean isBukkit() {
        return getBukkitEntity().isPresent();
    }

    /**
     * Get the location of the entity.
     * @return a non-null location.
     */
    @NotNull Location getLocation();

    /**
     * Get the location of the entity eyes.
     * @return a non-null location.
     */
    @NotNull Location getEyeLocation();

    /**
     * teleport the entity to another location.
     * @param location non-null location to use.
     */
    void teleport(@NotNull Location location);

    /**
     * Remove the entity from the game.
     * Will not do anything if already removed.
     */
    void remove();

    /**
     * Test if the entity can be removed
     * @return true if {@link #remove()} will do something.
     */
    boolean isValid();

    /**
     * Send a message to the entity. You probably don't want to implement this fully often.
     * @param component component to send.
     */
    void sendMessage(Component component);

    /**
     * Add a potion effect.
     * @param effect the potion effect to add. May do nothing.
     */
    void addPotionEffect(PotionEffect effect);

    /**
     * Get the NBT storage of the entity.
     * @return null if NBT entity is not valid.
     */
    default @Nullable PersistentDataContainer getNBT() {
        return getBukkitEntity().map(Entity::getPersistentDataContainer).orElse(null);
    }

    void setVelocity(@NotNull Vector vector);
}
