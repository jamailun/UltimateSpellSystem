package fr.jamailun.ultimatespellsystem.api.entities;

import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

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
     * Send a message to the entity.
     * @param component component to send.
     */
    void sendMessage(Component component);

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
     * Add a potion effect.
     * @param effect the potion effect to add. May do nothing.
     */
    void addPotionEffect(PotionEffect effect);
}
