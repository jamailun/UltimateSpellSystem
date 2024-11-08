package fr.jamailun.ultimatespellsystem.bukkit.utils.holders;

import fr.jamailun.ultimatespellsystem.bukkit.UltimateSpellSystem;
import org.bukkit.Location;
import org.bukkit.Particle;

import java.util.Map;

/**
 * Represents particles emitted by an orb.
 */
public class ParticleHolder {

    private final Particle type;
    private final double radius, speed;
    private final int count;

    public ParticleHolder(Particle type, double radius, double speed, int count) {
        this.type = type;
        this.radius = radius;
        this.speed = speed;
        this.count = count;
        UltimateSpellSystem.logDebug("New orb-particle : (" + this +")");
    }

    /**
     * Build an OrbParticle from a map.
     * @param context the debug location of the attributes, used for printing-purpose.
     * @param radius the radius for particles
     * @param values the map of attributes. Expected keys: {type, duration, power}
     * @return null if an error occurred.
     */
    public static ParticleHolder build(String context, double radius, Map<?, ?> values) {
        // Type
        Object typeRaw = values.get("type");
        if(!(typeRaw instanceof String type)) {
            UltimateSpellSystem.logError("(" + context + ") Invalid particle-type : '" + typeRaw + "'.");
            return null;
        }
        Particle particle;
        try {
            particle = Particle.valueOf(type.toUpperCase());
        } catch(IllegalArgumentException e) {
            UltimateSpellSystem.logError("(" + context + ") Unknown particle-type : '" + type + "' ("+e.getMessage()+")");
            return null;
        }

        // Speed
        double speed = 0.1;
        if(values.containsKey("speed")) {
            Object raw = values.get("speed");
            if(!(raw instanceof Double ds)) {
                UltimateSpellSystem.logError("(" + context + ") Invalid particle speed : '" + raw + "'.");
                return null;
            }
            speed = ds;
        }

        // Count
        int count = 1;
        if(values.containsKey("count")) {
            Object raw = values.get("count");
            if(!(raw instanceof Double di)) {
                UltimateSpellSystem.logError("(" + context + ") Invalid particle count : '" + raw + "'.");
                return null;
            }
            count = di.intValue();
        }

        // Create
        return new ParticleHolder(particle, radius, speed, count);
    }

    /**
     * Apply the particle-effect to a location.
     * @param location the non-ull location to use.
     */
    public void apply(Location location) {
        location.getWorld().spawnParticle(
                type, location, count,
                radius, radius, radius,
                speed
        );
    }

}
