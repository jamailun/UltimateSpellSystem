package fr.jamailun.ultimatespellsystem.bukkit.entities;

import org.bukkit.Particle;
import org.bukkit.potion.PotionEffect;

public class Orb extends CustomEntity {

    public Orb(SummonAttributes attributes) {
        super(attributes);
    }

    @Override
    protected void tickEntity(int ticksPeriod) {
        // Particules ?
        this.location.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, location, 5);

        // Effects ?
        System.out.println("j'exiiiiste ! " + this);
    }

    @Override
    public void addPotionEffect(PotionEffect effect) {}
}
