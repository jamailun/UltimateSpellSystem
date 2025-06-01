package fr.jamailun.ultimatespellsystem.extension.animations;

import fr.jamailun.ultimatespellsystem.api.animations.AnimationParticle;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.jetbrains.annotations.NotNull;

/**
 * A spiral going upward.
 */
public class AnimationParticleSpiraling extends AnimationParticle {

    @Getter private final long duration;
    private final Location center;
    private final double radius;
    protected final double heightPerTick;
    protected final double radiansPerTick;

    protected double bonusHeight = 0;
    @Setter private double theta = 0;

    public AnimationParticleSpiraling(long duration, @NotNull Particle particle, Location center, double radius, double degreesPerSecond, double heightPerSecond) {
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
}
