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
    }

    public static class CircleParticleShaper implements ParticleShaper {
        @Override
        public void apply(@NotNull Particle particle, @NotNull Location center, @NotNull @Unmodifiable Map<String, Object> data) {
            double radius = (double) data.getOrDefault("radius", 5);
            double delta = (double) data.getOrDefault("delta", .15);
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
            double radius = (double) data.getOrDefault("radius", 5);
            double delta = (double) data.getOrDefault("delta", .5);
            double phi = (double) data.getOrDefault("phi", .1);
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

}
