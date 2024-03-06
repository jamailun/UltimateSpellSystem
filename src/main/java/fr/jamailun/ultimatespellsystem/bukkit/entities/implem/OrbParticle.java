package fr.jamailun.ultimatespellsystem.bukkit.entities.implem;

import fr.jamailun.ultimatespellsystem.bukkit.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.bukkit.runner.nodes.functions.SendEffectNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Duration;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;

import java.util.Collection;
import java.util.Map;

public class OrbParticle {

    private final Particle type;
    private final double radius, speed;
    private final int count;

    OrbParticle(Particle type, double radius, double speed, int count) {
        this.type = type;
        this.radius = radius;
        this.speed = speed;
        this.count = count;
        UltimateSpellSystem.logDebug("New orb-particle : (" + this +")");
    }

    public static OrbParticle build(String context, double radius, Map<?, ?> values) {
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
        return new OrbParticle(particle, radius, speed, count);
    }

    public void apply(Location location) {
        location.getWorld().spawnParticle(
                type, location, count,
                radius, radius, radius,
                speed
        );
    }

    @Override
    public String toString() {
        return "OrbParticle{" +
                "type=" + type +
                ", radius=" + radius +
                ", speed=" + speed +
                ", count=" + count +
                '}';
    }
}
