package fr.jamailun.ultimatespellsystem.bukkit.entities;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class OrbArea extends CustomEntity {

    private final AreaEffectCloud area;

    public OrbArea(NewEntityAttributes attributes) {
        super(attributes);
        Location location = attributes.getLocation();

        area = location.getWorld().spawn(location, AreaEffectCloud.class, cloud -> {
            // Owner
            cloud.setOwnerUniqueId(attributes.getSummoner().getUniqueId());
            // Duration
            cloud.setDuration((int)attributes.getDuration().toTicks());
            cloud.setDurationOnUse(0);
            // Radius
            cloud.setRadius(attributes.tryGetAttribute("key", Double.class, 0.4).floatValue());
            cloud.setRadiusPerTick(0);
            cloud.setRadiusOnUse(0);
            // Effects : none !
            cloud.setParticle(Particle.BLOCK_CRACK, Material.AIR.createBlockData());
            cloud.addCustomEffect(new PotionEffect(PotionEffectType.BAD_OMEN, 0, 0, false, false, false), true);
        });
    }

    @Override
    public AreaEffectCloud getEntity() {
        return area;
    }

    @Override
    public void remove() {
        area.remove();
    }

    @Override
    public Vector getVelocity() {
        if(area.isValid())
            return area.getVelocity();
        return new Vector();
    }

    @Override
    public void setVelocity(@NotNull Vector velocity) {
        if(area.isValid())
            area.setVelocity(velocity);
    }

}
