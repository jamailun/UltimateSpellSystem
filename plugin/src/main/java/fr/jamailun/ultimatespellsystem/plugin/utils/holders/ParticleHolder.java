package fr.jamailun.ultimatespellsystem.plugin.utils.holders;

import fr.jamailun.ultimatespellsystem.UssLogger;
import fr.jamailun.ultimatespellsystem.api.utils.ParticleShaper;
import fr.jamailun.ultimatespellsystem.api.providers.ParticleShapeProvider;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * Represents particles emitted by an orb.
 */
public class ParticleHolder {

    private @Nullable final ShaperInstance shaper;
    private final Particle type;
    private final double radius, speed;
    private final int count;

    public ParticleHolder(@NotNull Particle type, double radius, double speed, int count, @Nullable ShaperInstance shaper) {
        this.type = type;
        this.radius = radius;
        this.speed = speed;
        this.count = count;
        this.shaper = shaper;
    }

    /**
     * Build an OrbParticle from a map.
     * @param context the debug location of the attributes, used for printing-purpose.
     * @param radius the radius for particles
     * @param values the map of attributes. Expected keys: {type, duration, power}
     * @return null if an error occurred.
     */
    @SuppressWarnings("unchecked")
    public static @Nullable ParticleHolder build(String context, double radius, @NotNull Map<?, ?> values) {
        // Type
        Object typeRaw = values.get("type");
        if(!(typeRaw instanceof String type)) {
            UssLogger.logError("(" + context + ") Invalid particle-type : '" + typeRaw + "'.");
            return null;
        }
        Particle particle;
        try {
            particle = Particle.valueOf(type.toUpperCase());
        } catch(IllegalArgumentException e) {
            UssLogger.logError("(" + context + ") Unknown particle-type : '" + type + "' ("+e.getMessage()+")");
            return null;
        }

        // Speed
        double speed = 0.1;
        if(values.containsKey("speed")) {
            Object raw = values.get("speed");
            if(!(raw instanceof Double ds)) {
                UssLogger.logError("(" + context + ") Invalid particle speed : '" + raw + "'.");
                return null;
            }
            speed = ds;
        }

        // Radius
        if(values.containsKey("radius")) {
            Object raw = values.get("radius");
            if(!(raw instanceof Double ds)) {
                UssLogger.logError("(" + context + ") Invalid particle radius : '" + raw + "'.");
                return null;
            }
            radius = ds;
        }

        // Count
        int count = 1;
        if(values.containsKey("count")) {
            Object raw = values.get("count");
            if(!(raw instanceof Double di)) {
                UssLogger.logError("(" + context + ") Invalid particle count : '" + raw + "'.");
                return null;
            }
            count = di.intValue();
        }

        // Shape ?
        ShaperInstance shaperInstance = null;
        if(values.containsKey("shape")) {
            ParticleShaper shaper;
            Object raw = values.get("shape");
            if(raw instanceof String string) {
                shaper = ParticleShapeProvider.instance().find(string);
                if(shaper == null) UssLogger.logWarning("Unknown particle shape: '" + string + "'.");
                else shaperInstance = new ShaperInstance(shaper, (Map<String, Object>) values);
            } else if(raw instanceof Map<?,?> map) {
                Object rawType = map.get("type");
                if(rawType instanceof String shapeType) {
                    shaper = ParticleShapeProvider.instance().find(shapeType);
                    if(shaper == null) UssLogger.logWarning("Unknown particle shape: '" + shapeType + "'.");
                    else shaperInstance = new ShaperInstance(shaper, (Map<String, Object>) map);
                } else {
                    UssLogger.logError("Particle shape missing the 'type' entry.");
                }
            }
        }

        // Create
        return new ParticleHolder(particle, radius, speed, count, shaperInstance);
    }

    /**
     * Apply the particle-effect to a location.
     * @param location the non-ull location to use.
     */
    public void apply(@NotNull Location location) {
        if(shaper == null) {
            location.getWorld().spawnParticle(
                    type, location, count,
                    radius, radius, radius,
                    speed
            );
        } else {
            shaper.run(type, location);
        }
    }

    public record ShaperInstance(@NotNull ParticleShaper shaper, @NotNull Map<String, Object> data) {
        void run(@NotNull Particle particle, @NotNull Location location) {
            shaper.apply(particle, location, data);
        }
    }

}
