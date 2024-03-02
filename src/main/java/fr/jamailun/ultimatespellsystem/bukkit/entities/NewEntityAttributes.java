package fr.jamailun.ultimatespellsystem.bukkit.entities;

import fr.jamailun.ultimatespellsystem.bukkit.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Duration;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * Attributes required to perform a summoning.
 */
public interface NewEntityAttributes {

    /**
     * Get the owner (and the cause) of the summoned entity.
     * @return the Entity doing the summoning.
     */
    @NotNull Entity getSummoner();

    /**
     * Get a map of attributes.
     * @return a immutable map.
     */
    @NotNull Map<String, Object> getAttributes();

    /**
     * Get a specific attribute.
     * @param key the key of the attribute.
     * @return null if the key is not set.
     */
    default @Nullable Object getAttribute(String key) {
        return getAttributes().get(key);
    }

    /**
     * Try to get a custom attribute.
     * @param key the key of the attribute.
     * @param clazz the class to cast the attribute to.
     * @param defaultValue the default value. Returned if the key is not set, <b>or</b> if the cast fails.
     * @return the default value OR a non-null value of required type.
     * @param <T> the generic to, for the returned value.
     */
    default <T> T tryGetAttribute(String key, Class<T> clazz, T defaultValue) {
        Object value = getAttribute(key);
        if(value == null)
            return defaultValue;
        try {
            return clazz.cast(value);
        } catch(ClassCastException e) {
            UltimateSpellSystem.logWarning("Summon tried to read attribute " + key + ": " + e.getMessage());
            return defaultValue;
        }
    }

    /**
     * Get the location of the summoning.
     * @return a non-null location.
     */
    @NotNull Location getLocation();

    /**
     * Get the type of the entity.
     * @return a non-null UssEntityType.
     */
    @NotNull UssEntityType getEntityType();

    /**
     * Get the lifetime of the future summoned creature.
     * @return a non-null duration.
     */
    @NotNull Duration getDuration();
}
