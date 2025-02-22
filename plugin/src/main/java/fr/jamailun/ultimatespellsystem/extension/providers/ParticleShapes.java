package fr.jamailun.ultimatespellsystem.extension.providers;

import fr.jamailun.ultimatespellsystem.api.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.api.entities.SpellEntity;
import fr.jamailun.ultimatespellsystem.api.providers.ParticleShapeProvider;
import fr.jamailun.ultimatespellsystem.api.utils.ParticleShaper;
import fr.jamailun.ultimatespellsystem.plugin.utils.ParticlesHelper;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Map;

/**
 * A registry for custom particle shapes.
 */
public final class ParticleShapes {
    private ParticleShapes() {}

    /**
     * Register the custom {@link ParticleShaper ParticleShapers}.
     */
    public static void register() {
        ParticleShapeProvider.instance().register(new SphereParticleShaper(), "sphere");
        ParticleShapeProvider.instance().register(new CircleParticleShaper(), "circle");
        ParticleShapeProvider.instance().register(new HalfSphereParticleShaper(), "half_sphere");
        ParticleShapeProvider.instance().register(new LineParticleShaper(), "line");
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

    /**
     * A shape of a line, from the location of the PLAY to a specific location target.
     */
    public static class LineParticleShaper implements ParticleShaper {
        @Override
        public void apply(@NotNull Particle particle, @NotNull Location location, @NotNull @Unmodifiable Map<String, Object> data) {
            double delta = getNumeric(data, "delta", .25d);
            Object targetRaw = data.get("target");
            if(targetRaw == null) {
                UltimateSpellSystem.logWarning("Particle shape 'line' is missing a 'target' property, of type LOCATION or ENTITY.");
                return;
            }
            Location target;
            if(targetRaw instanceof Location tgt) {
                target = tgt.clone();
            } else if(targetRaw instanceof SpellEntity entity) {
                target = entity.getLocation();
            } else {
                UltimateSpellSystem.logWarning("Particle shape 'line' expects property 'target' of type LOCATION or ENTITY. Got " + targetRaw.getClass() + ".");
                return;
            }
            ParticlesHelper.playLine(location, target, delta, particle);
        }
    }

}
