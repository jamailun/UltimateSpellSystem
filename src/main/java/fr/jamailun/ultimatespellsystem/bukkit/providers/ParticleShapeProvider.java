package fr.jamailun.ultimatespellsystem.bukkit.providers;

import fr.jamailun.ultimatespellsystem.api.bukkit.utils.ParticleShaper;
import fr.jamailun.ultimatespellsystem.bukkit.utils.ParticlesHelper;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Map;

public class ParticleShapeProvider extends UssProvider<ParticleShaper> {

    private static final ParticleShapeProvider INSTANCE = new ParticleShapeProvider();

    public static ParticleShapeProvider instance() {
        return INSTANCE;
    }

    static {
        INSTANCE.register(new SphereParticleShaper(), "sphere");
        INSTANCE.register(new CircleParticleShaper(), "circle");
        INSTANCE.register(new HalfSphereParticleShaper(), "half_sphere");
    }

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
