package fr.jamailun.ultimatespellsystem.plugin.animations;

import fr.jamailun.ultimatespellsystem.api.animations.AnimationParticle;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.jetbrains.annotations.NotNull;

public class AnimationParticleCircle extends AnimationParticle {

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
}
