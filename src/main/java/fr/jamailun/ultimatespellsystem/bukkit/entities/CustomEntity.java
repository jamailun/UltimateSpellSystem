package fr.jamailun.ultimatespellsystem.bukkit.entities;

import fr.jamailun.ultimatespellsystem.bukkit.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.bukkit.spells.SpellEntity;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

public abstract class CustomEntity implements SpellEntity {

    protected final UUID uuid = UUID.randomUUID();
    protected final Location location;
    protected final SummonAttributes attributes;
    private final BukkitRunnable runnable;


    protected Vector velocity = new Vector();
    private boolean valid = true;

    public CustomEntity(SummonAttributes attributes) {
        this.attributes = attributes;
        this.location = attributes.getLocation().clone();

        int ticksPeriod = 5;
        runnable = UltimateSpellSystem.runTaskRepeat(() -> tick(ticksPeriod), 0, ticksPeriod);
    }


    public final void tick(int ticksPeriod) {
        if(!isValid())
            return;

        // Movement
        this.location.add(velocity.clone().multiply( (double)ticksPeriod/20d));

        // Tick
        tickEntity(ticksPeriod);
    }

    protected abstract void tickEntity(int ticks);

    @Override
    public UUID getUniqueId() {
        return uuid;
    }

    @Override
    public Optional<Entity> getBukkitEntity() {
        return Optional.empty();
    }

    @Override
    public @NotNull Location getLocation() {
        return location;
    }

    @Override
    public void teleport(@NotNull Location location) {
        this.location.set(location.x(), location.y(), location.z());
        this.location.setPitch(location.getPitch());
        this.location.setYaw(location.getYaw());
    }

    @Override
    public void sendMessage(Component component) {
        // Nothing
    }

    @Override
    public void remove() {
        valid = false;
        if( ! runnable.isCancelled()) {
            runnable.cancel();
        }
    }

    @Override
    public boolean isValid() {
        return valid;
    }

    public @NotNull Vector getVelocity() {
        return velocity;
    }

    public void setVelocity(@NotNull Vector velocity) {
        this.velocity = velocity;
    }
}
