package fr.jamailun.ultimatespellsystem.api.bukkit.providers;

import fr.jamailun.ultimatespellsystem.api.bukkit.utils.ParticleShaper;
import fr.jamailun.ultimatespellsystem.bukkit.utils.ParticlesHelper;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Map;

/**
 * A provider for {@link ParticleShaper}, used by particle emissions and animations.
 */
public final class ParticleShapeProvider extends UssProvider<ParticleShaper> {

    private static final ParticleShapeProvider INSTANCE = new ParticleShapeProvider();

    /**
     * Get the non-null instance.
     * @return the instance.
     */
    public static @NotNull ParticleShapeProvider instance() {
        return INSTANCE;
    }

    static {
        INSTANCE.register(new SphereParticleShaper(), "sphere");
        INSTANCE.register(new CircleParticleShaper(), "circle");
        INSTANCE.register(new HalfSphereParticleShaper(), "half_sphere");
    }

    /**
     * A shape to emit particle on a X/Z circle.
     */
    public static class CircleParticleShaper implements ParticleShaper {
        @Override
        public void apply(@NotNull Particle particle, @NotNull Location center, @NotNull @Unmodifiable Map<String, Object> data) {
            double radius = getNumeric(data, "radius", 5d);
            double delta = getNumeric(data, "delta", .15d);
            ParticlesHelper.playCircleXZ(
                    center.getWorld().getPlayers(),
                    center,
                    radius,
                    delta,
                    particle
            );
        }
    }

    /**
     * A shape to emit particle to a full sphere surface.
     */
    public static class SphereParticleShaper implements ParticleShaper {
        @Override
        public void apply(@NotNull Particle particle, @NotNull Location center, @NotNull @Unmodifiable Map<String, Object> data) {
            double radius = getNumeric(data, "radius", 5d);
            double delta = getNumeric(data, "delta", .5d);
            double phi = getNumeric(data, "phi", .1d);
            ParticlesHelper.playSphere(
                    center.getWorld().getPlayers(),
                    center,
                    radius,
                    delta,
                    phi,
                    particle
            );
        }
    }

    /**
     * A shape to emit particles to a half sphere (the top part)
     */
    public static class HalfSphereParticleShaper implements ParticleShaper {
        @Override
        public void apply(@NotNull Particle particle, @NotNull Location center, @NotNull @Unmodifiable Map<String, Object> data) {
            double radius = getNumeric(data, "radius", 5d);
            double delta = getNumeric(data, "delta", .5d);
            double phi = getNumeric(data, "phi", .1d);
            ParticlesHelper.playHalfSphere(
                    center.getWorld().getPlayers(),
                    center,
                    radius,
                    delta,
                    phi,
                    particle
            );
        }
    }

}
