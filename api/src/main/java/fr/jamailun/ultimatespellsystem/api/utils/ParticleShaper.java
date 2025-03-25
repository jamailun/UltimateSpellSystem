package fr.jamailun.ultimatespellsystem.api.utils;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Map;

/**
 * Describes something that can spawn particles.
 */
public interface ParticleShaper {

    /**
     * Summon the particles for all players.
     * @param particle the tyê of particle to spawn.
     * @param location the location to summon the particles at (or around, depending on the implementation).
     * @param data the data to use.
     */
    void apply(@NotNull Particle particle, @NotNull Location location, @NotNull @Unmodifiable Map<String, Object> data);

    /**
     * Get a numeric value from a data map.
     * @param data the data map to read from.
     * @param key the key to read in the map.
     * @param defaultValue the value to return if the key does not exist (or if the value does not match the Number type).
     * @return either the default value, or the found value.
     */
    default double getNumeric(@NotNull @Unmodifiable Map<String, Object> data, @NotNull String key, double defaultValue) {
        Object raw = data.get(key);
        if(raw instanceof Number number) {
            return number.doubleValue();
        }
        return defaultValue;
    }

}
