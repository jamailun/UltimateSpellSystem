package fr.jamailun.ultimatespellsystem.extension.animations;

import fr.jamailun.ultimatespellsystem.UssLogger;
import fr.jamailun.ultimatespellsystem.api.animations.AnimationParticle;
import fr.jamailun.ultimatespellsystem.api.providers.AnimationsProvider;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Duration;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.jetbrains.annotations.NotNull;

/**
 * A spiral going upward.
 */
public class AnimationParticleSpiraling extends AnimationParticle {

    public static final String ID = "particle.spiral";

    @Getter private final long duration;
    private final Location center;
    private final double radius;
    protected final double heightPerTick;
    protected final double radiansPerTick;

    protected double bonusHeight = 0;
    @Setter private double theta = 0;

    public AnimationParticleSpiraling(long duration, @NotNull Particle particle, @NotNull Location center, double radius, double degreesPerSecond, double heightPerSecond) {
        super(particle);
        this.duration = duration;
        this.center = center;
        this.radius = radius;
        this.radiansPerTick = Math.toRadians(degreesPerSecond) / 20d;
        this.heightPerTick = heightPerSecond / 20d;
    }

    @Override
    protected void onTick() {
        double thetaMax = theta + radiansPerTick;
        for(; theta < thetaMax; theta += precisionRadians) {
            double dx = radius * Math.cos(theta);
            double dy = radius * Math.sin(theta);
            Location position = center.clone().add(dx, bonusHeight, dy);
            position.getWorld().spawnParticle(particle, position, 1, 0d, 0d, 0d, 0);
        }
        bonusHeight += heightPerTick;
        theta += radiansPerTick;
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
                double degPerSec = Helper.asOpt(data, Number.class, "speed.theta", ID, 72).doubleValue();
                double heighPerSec = Helper.asOpt(data, Number.class, "speed.y", ID, 1).doubleValue();
                return new AnimationParticleSpiraling(duration.toTicks(), particle, location, radius, degPerSec, heighPerSec);
            } catch (Helper.MissingProperty | Helper.BadProperty e) {
                UssLogger.logError(e.getMessage());
            } catch(IllegalArgumentException e) {
                UssLogger.logError("Animation: unknown Material. " + e.getMessage());
            }
            return null;
        };
    }
}
