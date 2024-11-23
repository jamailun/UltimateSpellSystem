package fr.jamailun.ultimatespellsystem.extension.providers;

import fr.jamailun.ultimatespellsystem.api.providers.ParticleShapeProvider;
import fr.jamailun.ultimatespellsystem.api.utils.ParticleShaper;
import fr.jamailun.ultimatespellsystem.plugin.utils.ParticlesHelper;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Map;

public final class ParticleShapes {
    private ParticleShapes() {}

    public static void register() {
        ParticleShapeProvider.instance().register(new SphereParticleShaper(), "sphere");
        ParticleShapeProvider.instance().register(new CircleParticleShaper(), "circle");
        ParticleShapeProvider.instance().register(new HalfSphereParticleShaper(), "half_sphere");
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
