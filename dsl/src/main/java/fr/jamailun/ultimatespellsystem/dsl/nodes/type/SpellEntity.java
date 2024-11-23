package fr.jamailun.ultimatespellsystem.dsl.nodes.type;

import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

/**
 * An entity-representation inside a spell.
 */
public interface SpellEntity {

    /**
     * The Bukkit unique ID of the entity.
     * @return a non-null value.
     */
    @NotNull UUID getUniqueId();

    /**
     * Get the bukkit entity.
     * @return a non-null optional.
     */
    @Contract(pure = true)
    @NotNull Optional<Entity> getBukkitEntity();

    /**
     * Test is the entity exist within Bukkit.
     * @return true if it's a Bukkit entity.
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
     * Get the eye-location of the entity.
     * @return a non-null location.
     */
    @NotNull Location getEyeLocation();

    /**
     * Teleport the entity to another location.
     * @param location the non-null location to use.
     */
    void teleport(@NotNull Location location);

    /**
     * Send a message.
     * @param component the message to send.
     */
    void sendMessage(@NotNull Component component);

    /**
     * Remove the entity from the world.
     */
    void remove();

    /**
     * Test ig the entity is valid, i.e. still exists and alive.
     * @return true if the entity is alive and well.
     */
    boolean isValid();

    /**
     * Add a potion effect.
     * @param effect the effect to add.
     */
    void addPotionEffect(@NotNull PotionEffect effect);
}
