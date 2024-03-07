package fr.jamailun.ultimatespellsystem.bukkit.entities.implem;

import fr.jamailun.ultimatespellsystem.bukkit.entities.CustomEntity;
import fr.jamailun.ultimatespellsystem.bukkit.entities.SummonAttributes;
import fr.jamailun.ultimatespellsystem.bukkit.utils.holders.ParticleHolder;
import fr.jamailun.ultimatespellsystem.bukkit.utils.holders.PotionEffectHolder;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.Vector;

import java.util.*;

public class Orb extends CustomEntity {

    private final double radius;
    private final List<ParticleHolder> particles = new ArrayList<>();
    private final List<PotionEffectHolder> effects = new ArrayList<>();
    private boolean hasEffects = false;
    private final boolean autoApply;


    private final static String CTX = "ORB.attributes.";
    public Orb(SummonAttributes attributes) {
        super(attributes);

        // Radius && auto-apply
        radius = attributes.tryGetAttribute("radius", Double.class, 0.4d);
        autoApply = attributes.tryGetAttribute("autoApply", Boolean.class, false);

        // Particles (MONO)
        Map<?,?> particleMap = attributes.tryGetAttribute("particle", Map.class, null);
        if(particleMap != null) {
            ParticleHolder op = ParticleHolder.build(CTX+"particle", radius, particleMap);
            if(op != null)
                particles.add(op);
        }
        // Particles (MULTI)
        if(attributes.hasAttribute("particles")) {
            for(Map<?,?> map : attributes.tryGetAttributes("particles", Map.class)) {
                ParticleHolder op = ParticleHolder.build(CTX+"particles", radius, map);
                if(op != null) {
                    particles.add(op);
                }
            }
        }

        // Effects (MONO)
        Map<?,?> effectMap = attributes.tryGetAttribute("effect", Map.class, null);
        if(effectMap != null) {
            PotionEffectHolder oe = PotionEffectHolder.build(CTX+"effect", effectMap);
            if(oe != null) {
                effects.add(oe);
                hasEffects = true;
            }
        }
        // Effects (MULTI)
        if(attributes.hasAttribute("effects")) {
            for(Map<?,?> mapEffect : attributes.tryGetAttributes("effects", Map.class)) {
                PotionEffectHolder effect = PotionEffectHolder.build(CTX+"effects", mapEffect);
                if(effect != null) {
                    effects.add(effect);
                    hasEffects = true;
                }
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
        // Particules
        particles.forEach(p -> p.apply(location));

        // Effects
        if(hasEffects) {
            Collection<LivingEntity> nearby = location.getNearbyLivingEntities(radius);
            if(!autoApply)
                nearby.removeIf(le -> le.getUniqueId().equals(attributes.getSummoner().getUniqueId()));
            effects.forEach(o -> o.apply(nearby));
        }

    }

    @Override
    public void addPotionEffect(PotionEffect effect) {}
}
