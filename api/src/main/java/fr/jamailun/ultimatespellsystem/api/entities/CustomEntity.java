package fr.jamailun.ultimatespellsystem.api.entities;

import fr.jamailun.ultimatespellsystem.api.UltimateSpellSystem;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

/**
 * A Custom Entity is an implementation of {@link SpellEntity} for non-bukkit ones.
 */
@Getter
public abstract class CustomEntity implements SpellEntity {

    protected final UUID uuid = UUID.randomUUID();
    protected final Location location;
    protected final SummonAttributes attributes;
    private final BukkitRunnable runnable;

    @Setter protected @NotNull Vector velocity = new Vector();
    private boolean valid = true;

    /**
     * Create a new custom entity. All entities are summons, so {@link SummonAttributes} are required.
     * @param attributes the attributes the custom entity is from.
     */
    public CustomEntity(@NotNull SummonAttributes attributes) {
        this.attributes = attributes;
        this.location = attributes.getLocation().clone();

        int ticksPeriod = Math.max(1, attributes.tryGetAttribute("_clock", Double.class, 5d).intValue());
        runnable = UltimateSpellSystem.runTaskRepeat(() -> tick(ticksPeriod), 0, ticksPeriod);
    }

    /**
     * Tick the entity.
     * @param ticksPeriod the amount of ticks since the last tick.
     */
    public final void tick(int ticksPeriod) {
        if(!isValid())
            return;

        // Movement
        this.location.add(velocity.clone().multiply( (double)ticksPeriod/20d));

        // Tick
        tickEntity(ticksPeriod);
    }

    /**
     * Implementation-specific tick.
     * @param ticks the amount of ticks since the last tick.
     */
    protected abstract void tickEntity(int ticks);

    @Override
    public @NotNull UUID getUniqueId() {
        return uuid;
    }

    @Override
    public @NotNull Optional<Entity> getBukkitEntity() {
        return Optional.empty();
    }

    @Override
    public @NotNull Location getEyeLocation() {
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
}
