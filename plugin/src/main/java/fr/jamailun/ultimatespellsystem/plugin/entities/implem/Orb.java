package fr.jamailun.ultimatespellsystem.plugin.entities.implem;

import fr.jamailun.ultimatespellsystem.api.entities.CustomEntity;
import fr.jamailun.ultimatespellsystem.api.entities.SummonAttributes;
import fr.jamailun.ultimatespellsystem.api.runner.errors.InvalidTypeException;
import fr.jamailun.ultimatespellsystem.plugin.utils.holders.ParticleHolder;
import fr.jamailun.ultimatespellsystem.plugin.utils.holders.PotionEffectHolder;
import fr.jamailun.ultimatespellsystem.plugin.utils.holders.SoundHolder;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * A flying orb. Pass through walls.
 */
public class Orb extends CustomEntity {

    // Configuration
    private final double radius;
    private final List<ParticleHolder> particles = new ArrayList<>();
    private final List<PotionEffectHolder> effects = new ArrayList<>();
    private boolean hasEffects = false;
    private final boolean applySelf;
    private final int fireTicks;
    private final double damages;
    private SoundHolder hitSound;
    private final int maxCollisionsEntity;

    // Runtime
    private final Set<UUID> receivedEffects = new HashSet<>();

    private final static String CTX = "ORB.attributes.";
    public Orb(SummonAttributes attributes) {
        super(attributes);

        // Radius && auto-apply
        radius = attributes.tryGetAttribute("radius", Double.class, 0.4d);
        applySelf = attributes.tryGetAttribute("apply_self", Boolean.class, false);

        // Sound (launch)
        Map<?,?> spawnSoundMap = attributes.tryGetAttribute("sound", Map.class);
        SoundHolder spawnSound = null;
        if(spawnSoundMap != null)
            spawnSound = SoundHolder.build("orb.sound", spawnSoundMap);

        // Sound (hit)
        Map<?,?> hitSoundMap = attributes.tryGetAttribute("hit_sound", Map.class);
        if(hitSoundMap != null) {
            hitSound = SoundHolder.build("orb.hit_sound", hitSoundMap);
        }
        if(hitSound == null) hitSound = SoundHolder.NONE;

        // Particles (MONO)
        Map<?,?> particleMap = attributes.tryGetAttribute("particle", Map.class);
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
        Map<?,?> effectMap = attributes.tryGetAttribute("effect", Map.class);
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

        // Fire + damages
        fireTicks = attributes.tryGetAttribute("fire", Double.class, 0d).intValue();
        damages = attributes.tryGetAttribute("damages", Double.class, 0d);
        if(fireTicks > 0 || damages > 0)
            hasEffects = true;
        maxCollisionsEntity = attributes.tryGetAttribute("max_collisions", Double.class, 0d).intValue();

        // Velocity
        double speed = attributes.tryGetAttribute("velocity", Double.class, 0d);
        if(speed != 0) {
            Vector dir;
            if(attributes.hasAttribute("direction")) {
                dir = extractDirection(attributes.getAttribute("direction"));
            } else {
                dir = attributes.getSummoner().getLocation().getDirection().clone();
            }
            Vector velocity = dir.normalize().multiply(speed);
            setVelocity(velocity);
        }

        // Spawn effect
        if(spawnSound != null)
            spawnSound.apply(attributes.getLocation());
    }

    @Override
    protected void tickEntity(int ticksPeriod) {
        // Particules
        particles.forEach(p -> p.apply(location));

        // Effects
        if(hasEffects) {
            Collection<LivingEntity> nearby = location.getNearbyLivingEntities(radius, le -> !receivedEffects.contains(le.getUniqueId()));
            if(!nearby.isEmpty()) {
                applyEffects(nearby);
                if(maxCollisionsEntity > 0 && receivedEffects.size() >= maxCollisionsEntity) {
                    remove();
                }
            }
        }
    }

    private void applyEffects(@NotNull Collection<LivingEntity> nearby) {
        for(LivingEntity other : nearby) {
            // Ignore owner ?
            if(!applySelf && other.getUniqueId().equals(attributes.getSummoner().getUniqueId()))
                continue;
            // Register UUID, ignore entity if already exists.
            if(!receivedEffects.add(other.getUniqueId()))
                continue;

            // Potion effects
            effects.forEach(o -> o.apply(other));
            // Fire + Damages
            if(fireTicks > 0)
                nearby.forEach(e -> e.setFireTicks(e.getFireTicks() + fireTicks));
            if(damages > 0)
                nearby.forEach(e -> e.damage(damages, getAttributes().getSummoner()));

            // Hit effect
            hitSound.apply(other.getLocation());
        }
    }

    @Override
    public void addPotionEffect(PotionEffect effect) {
        // This orb cannot receive potion effect.
    }

    private static Vector extractDirection(Object raw) {
        if(raw instanceof Location loc) {
            return loc.toVector();
        }

        if(raw instanceof Vector vec) {
            return vec.clone();
        }

        if(raw instanceof List<?> list) {
            double dx = 0, dy = 0, dz = 0;
            int i = 0;
            for(; i < Math.min(3, list.size()); i++) {
                if(!(list.get(i) instanceof Number num))
                    throw new InvalidTypeException("orb.attributes.direction", "direction.list["+i+"]", list.get(i));
                if(i == 0) dx = num.doubleValue();
                if(i == 1) dy = num.doubleValue();
                if(i == 2) dz = num.doubleValue();
            }
            if(i < 1) dx = 0;
            if(i < 2) dy = dx;
            if(i < 3) dz = dy;
            return new Vector(dx, dy, dz);
        }

        throw new InvalidTypeException("orb.attributes.direction", "direction(location/vector/list)", raw);
    }
}
