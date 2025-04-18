package fr.jamailun.ultimatespellsystem.api.animations;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Particle;
import org.jetbrains.annotations.NotNull;

/**
 * An abstract class, providing access to particle-oriented animations.
 */
public abstract class AnimationParticle extends Animation {

    /** Useful math value. */
    protected final static double PI_SQUARE = Math.PI * 2;
    /** Useful math value. */
    protected final static double PI_2 = Math.PI / 2;

    /**
     * Particle type to display.
     */
    protected final Particle particle;

    /**
     * The frequency to spawn particle at.
     * A frequency of {@code N} means the particles are spawned every {@code N} ticks.
     */
    @Getter @Setter
    protected int refreshFrequencyTicks = 5;

    /**
     * The precision to have.
     * A precision of {@code N} means one particle will be spawned every {@code N}° on the circle.
     */
    @Getter
    protected double precisionRadians = Math.toRadians(360d / 32); // 32 particles per 360°

    /**
     * Create a new animation-particle.
     * @param particle the particle to use.
     */
    public AnimationParticle(@NotNull Particle particle) {
        this.particle = particle;
    }

    /**
     * Change the precision. A precision of {@code N} means one particle will be spawned every {@code N}° on the circle.
     * @param precisionDegree an angle in degrees.
     */
    public void setPrecisionDegree(double precisionDegree) {
        if(precisionDegree <= 0.1)
            throw new IllegalArgumentException("Too small, degree should be <= 0.1 !");
        if(precisionDegree > 90)
            throw new IllegalArgumentException("Too big, are you sure this value is expected ("+precisionDegree+") ?");
        this.precisionRadians = Math.toRadians(precisionDegree);
    }

    @Override
    protected void onStart() {}

    @Override
    public void onFinish() {}
}
