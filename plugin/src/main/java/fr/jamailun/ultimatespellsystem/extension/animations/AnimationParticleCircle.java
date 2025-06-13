package fr.jamailun.ultimatespellsystem.extension.animations;

import fr.jamailun.ultimatespellsystem.UssLogger;
import fr.jamailun.ultimatespellsystem.api.animations.AnimationParticle;
import fr.jamailun.ultimatespellsystem.api.providers.AnimationsProvider;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Duration;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.jetbrains.annotations.NotNull;

public class AnimationParticleCircle extends AnimationParticle {

    public static final String ID = "particle.circle";

    @Getter private final long duration;
    private final Location center;
    private final double radius;

    public AnimationParticleCircle(long duration, @NotNull Particle particle, @NotNull Location center, double radius) {
        super(particle);
        this.duration = duration;
        this.center = center;
        this.radius = radius;
    }

    @Override
    protected void onTick() {
        if((getTicks()-1) % refreshFrequencyTicks != 0)
            return;

        for(double theta = 0; theta < Math.PI * 2; theta += precisionRadians) {
            double dx = radius * Math.cos(theta);
            double dy = radius * Math.sin(theta);
            Location position = center.clone().add(dx, 0, dy);
            position.getWorld().spawnParticle(particle, position, 1, 0d, 0d, 0d, 0);
        }
    }

    /**
     * Generate an animation generator.
     * @return a new generator.
     */
    public static @NotNull AnimationsProvider.AnimationGenerator generator() {
        return (location, data) -> {
            try {
                Duration duration = Helper.as(data, Duration.class, "duration", ID);
                double radius = Helper.asOpt(data, Number.class, "radius", ID, 1.0).doubleValue();
                Particle particle = Helper.asEnum(data, Particle.class, "particle", ID);
                return new AnimationParticleCircle(duration.toTicks(), particle, location, radius);
            } catch (Helper.MissingProperty | Helper.BadProperty e) {
                UssLogger.logError(e.getMessage());
            } catch(IllegalArgumentException e) {
                UssLogger.logError("Animation: unknown Material. " + e.getMessage());
            }
            return null;
        };
    }
}
