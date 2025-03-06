package fr.jamailun.ultimatespellsystem.plugin.entities.implem;

import fr.jamailun.ultimatespellsystem.api.entities.CustomEntity;
import fr.jamailun.ultimatespellsystem.api.entities.SummonAttributes;
import fr.jamailun.ultimatespellsystem.plugin.utils.Point;
import fr.jamailun.ultimatespellsystem.plugin.utils.TypeInterpretation;
import fr.jamailun.ultimatespellsystem.plugin.utils.holders.ParticleHolder;
import fr.jamailun.ultimatespellsystem.plugin.utils.holders.PotionEffectHolder;
import fr.jamailun.ultimatespellsystem.plugin.utils.holders.SoundHolder;
import org.bukkit.block.Block;
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
    private SoundHolder spawnSound = null;
    private final double radius;
    private final List<ParticleHolder> particles = new ArrayList<>();
    private final List<PotionEffectHolder> effects = new ArrayList<>();
    private boolean hasEffects = false;
    private final boolean applySelf;
    private final int fireTicks;
    private final double damages;
    private SoundHolder hitSound;
    private final int maxCollisionsEntity;
    private final int maxBlockTraversal;

    // Runtime
    private final Set<UUID> receivedEffects = new HashSet<>();
    private final Set<Point> traversedBlocks = new HashSet<>();

    private final static String CTX = "ORB.attributes.";
    public Orb(SummonAttributes attributes) {
        super(attributes);
        // Radius && auto-apply
        radius = attributes.tryGetAttribute("radius", Double.class, 0.4d);
        applySelf = attributes.tryGetAttribute("apply_self", Boolean.class, false);

        // Fire + damages
        fireTicks = attributes.tryGetInt("fire", 0);
        damages = attributes.tryGetAttribute("damages", Double.class, 0d);
        if(fireTicks > 0 || damages > 0)
            hasEffects = true;
        maxCollisionsEntity = attributes.tryGetInt("max_collisions", 0);
        maxBlockTraversal = attributes.tryGetInt("max_blocks_hit", -1);

        // Other
        parseAttributes();

        // Spawn effect
        if(spawnSound != null)
            spawnSound.apply(attributes.getLocation());
    }

    private void parseAttributes() {
        // Sounds
        spawnSound = attributes.parseMap("sound", map -> SoundHolder.build("orb.sound", map));
        hitSound = attributes.parseMap("hit_sound", map -> SoundHolder.build("orb.hit_sound", map));
        if(hitSound == null) hitSound = SoundHolder.NONE;

        // Particles
        particles.addAll(attributes.parseMap("particle", (map, key) -> ParticleHolder.build(CTX + key, radius, map)));

        // Effects
        effects.addAll(attributes.parseMap("effect", (map,key) -> PotionEffectHolder.build(CTX+key, map)));
        hasEffects = !effects.isEmpty();

        // Velocity and direction
        double speed = attributes.tryGetAttribute("velocity", Double.class, 0d);
        if(speed != 0) {
            Vector dir;
            if(attributes.hasAttribute("direction")) {
                dir = TypeInterpretation.extractDirection(attributes.getAttribute("direction"));
            } else {
                dir = attributes.getSummoner().getLocation().getDirection().clone();
            }
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
            Collection<LivingEntity> nearby = location.getNearbyLivingEntities(radius, le -> !receivedEffects.contains(le.getUniqueId()));
            if(!nearby.isEmpty()) {
                applyEffects(nearby);
                if(maxCollisionsEntity > 0 && receivedEffects.size() >= maxCollisionsEntity) {
                    remove();
                }
            }
        }
        // Can remove the orb after too many block traversals.
        if(maxBlockTraversal >= 0) {
            checkForBlockTraversal();
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

    private void checkForBlockTraversal() {
        // Only 'hit' if non-air.
        Block current = location.getBlock();
        if(current.getType().isAir())
            return;

        // Try to add point.
        if(traversedBlocks.add(Point.fromLocation(location))) {
            if (traversedBlocks.size() > maxBlockTraversal) {
                remove();
            }
        }
    }

    @Override
    public void addPotionEffect(PotionEffect effect) {
        // This orb cannot receive potion effect.
    }

}
