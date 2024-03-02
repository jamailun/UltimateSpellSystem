package fr.jamailun.ultimatespellsystem.bukkit.entities;

import fr.jamailun.ultimatespellsystem.bukkit.UltimateSpellSystem;
import org.bukkit.Particle;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.Vector;

public class Orb extends CustomEntity {

    private final double radius;
    private Particle particle;
    private int particleCount;
    private double particleSpeed;

    public Orb(SummonAttributes attributes) {
        super(attributes);

        // Radius
        radius = attributes.tryGetAttribute("radius", Double.class, 0.4d);

        // Particle
        Object p = attributes.getAttribute("particle");
        UltimateSpellSystem.logDebug("Orb.data : " + attributes.getAttributes());
        if(p instanceof String ps) {
            try {
                particle = Particle.valueOf(ps.toUpperCase());
                particleCount = attributes.tryGetAttribute("particleCount", Double.class, 1d).intValue();
                particleSpeed = attributes.tryGetAttribute("particleSpeed", Double.class, 1d);
            } catch (IllegalArgumentException ignored) {
                UltimateSpellSystem.logWarning("Orb.particle : unknown particle '" + p + "'.");
            }
        }

        // Velocity
        double speed = attributes.tryGetAttribute("velocity", Double.class, 0d);
        if(speed != 0) {
            Vector dir = attributes.getSummoner().getLocation().getDirection().clone();
            Vector velocity = dir.normalize().multiply(speed);
            setVelocity(velocity);
        }

    }

    @Override
    protected void tickEntity(int ticksPeriod) {
        // Particules ?
        if(particle != null) {
            //TODO check if it's better to send them client-side.
            location.getWorld().spawnParticle(
                    particle, location, particleCount,
                    radius, radius, radius,
                    particleSpeed
            );
        }
    }

    @Override
    public void addPotionEffect(PotionEffect effect) {}
}
