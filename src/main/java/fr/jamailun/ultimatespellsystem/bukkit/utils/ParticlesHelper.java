package fr.jamailun.ultimatespellsystem.bukkit.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Set;

/**
 * Internal helper for shaped particles rendering.
 */
public final class ParticlesHelper {
    private ParticlesHelper() {}

    public static void playSphere(@NotNull Location center, double radius, double delta, double precision, @NotNull Particle particle) {
        playSphere(Bukkit.getOnlinePlayers(), center, radius, delta, precision, particle);
    }

    public static void playSphere(@NotNull Collection<? extends Player> targets, @NotNull Location center, double radius, double delta, double deltaPhi, @NotNull Particle particle) {
        playSphere(targets, center, radius, delta, deltaPhi, particle, 0, Math.PI);
    }
    public static void playHalfSphere(@NotNull Collection<? extends Player> targets, @NotNull Location center, double radius, double delta, double deltaPhi, @NotNull Particle particle) {
        playSphere(targets, center, radius, delta, deltaPhi, particle, Math.PI / 2, Math.PI);
    }

    public static void playSphere(@NotNull Collection<? extends Player> targets, @NotNull Location center, double radius, double delta, double deltaPhi, @NotNull Particle particle, double phiStart, double phiEnd) {
        for(double phi = phiStart; phi <= phiEnd; phi += deltaPhi) {
            double dY = (2 * phi * radius / Math.PI) - radius;
            Location centerXZ = center.clone().add(0, dY, 0);
            double r = Math.sin(phi) * radius;
            playCircleXZ(targets, centerXZ, r, delta, particle);
        }
        playCircleXZ(targets, center, radius, delta / 4, particle);
    }

    public static void playCircleXZ(@NotNull Collection<? extends Player> targets, @NotNull Location center, double radius, double delta, @NotNull Particle particle) {
        assert delta > 0;
        assert radius > 0;
        for(double theta = 0; theta < Math.PI * 2; theta += delta) {
            double cos = radius * Math.cos(theta);
            double sin = radius * Math.sin(theta);
            targets.forEach(p -> p.spawnParticle(
                    particle,
                    center.getX() + cos, center.getY(), center.getZ() + sin,
                    1, // count
                    0, 0, 0, // offset
                    0 // speed
            ));
        }
    }

    public static void playCircleXZ(@NotNull Player target, @NotNull Location center, double radius, double delta, @NotNull Particle particle) {
        playCircleXZ(Set.of(target), center, radius, delta, particle);
    }

    public static void playLine(@NotNull Collection<? extends Player> targets, @NotNull Location a, @NotNull Location b, double delta, @NotNull Particle particle) {
        Vector dir = b.toVector().subtract(a.toVector()).normalize();
        double dist = a.distance(b);
        for(double d = 0; d <= dist; d += delta) {
            double fd = d;
            targets.forEach(p -> p.spawnParticle(
                    particle,
                    a.getX() + (dir.getX() * fd), a.getY() + (dir.getY() * fd), a.getZ() + (dir.getZ() * fd),
                    1, // count
                    0, 0, 0, // offset
                    0 // speed
            ));
        }
    }

    public static void playLine(@NotNull Location a, @NotNull Location b, double delta, Particle particle) {
        playLine(Bukkit.getOnlinePlayers(), a, b, delta, particle);
    }

    public static void playBox(Collection<? extends Player> targets, @NotNull Location first, @NotNull Location second, double delta, @NotNull Particle particle) {
        Location min = new Location(
                first.getWorld(),
                Math.min(first.getX(), second.getX()),
                Math.min(first.getY(), second.getY()),
                Math.min(first.getZ(), second.getZ())
        );
        Location max = new Location(
                first.getWorld(),
                Math.max(first.getX(), second.getX()),
                Math.max(first.getY(), second.getY()),
                Math.max(first.getZ(), second.getZ())
        );

        Vector dx = new Vector(max.getX() - min.getX(), 0, 0);
        Vector dy = new Vector(0, max.getY() - min.getY(), 0);
        Vector dz = new Vector(0, 0, max.getZ() - min.getZ());

        Location b = min.clone().add(dx);
        Location c = b.clone().add(dz);
        Location d = min.clone().add(dz);
        Location e = min.clone().add(dy);
        Location f = b.clone().add(dy);
        Location h = d.clone().add(dy);
        // lower plane
        playLine(targets, min, b, delta, particle);
        playLine(targets, min, d, delta, particle);
        playLine(targets, b, c, delta, particle);
        playLine(targets, d, c, delta, particle);
        // upper place
        playLine(targets, e, f, delta, particle);
        playLine(targets, e, h, delta, particle);
        playLine(targets, f, max, delta, particle);
        playLine(targets, h, max, delta, particle);
        // pillars
        playLine(targets, min, e, delta, particle);
        playLine(targets, b, f, delta, particle);
        playLine(targets, c, max, delta, particle);
        playLine(targets, d, h, delta, particle);
    }

}
