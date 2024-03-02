package fr.jamailun.ultimatespellsystem.bukkit.entities;

import org.bukkit.*;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class Orb {

    private Vector velocity; // blocks per seconds
    private final UUID uuid = UUID.randomUUID();

    private final BoundingBox boundingBox;
    private final World world;

    public Orb(Location location, double radius) {
        boundingBox = BoundingBox.of(location, radius, radius, radius);
        world = location.getWorld();
    }

    public @NotNull Location getLocation() {
        return boundingBox.getCenter().toLocation(world);
    }

    /**
     * Get the velocity.
     * @param vector a vector : unit is "blocks/second".
     */
    public void setVelocity(@NotNull Vector vector) {
        this.velocity = vector.clone();
    }

    public @NotNull Vector getVelocity() {
        return velocity;
    }

    public void move(double deltaSeconds) {
        boundingBox.shift(
                velocity.getX() * deltaSeconds,
                velocity.getY() * deltaSeconds,
                velocity.getZ() * deltaSeconds
        );
    }

    public @NotNull BoundingBox getBoundingBox() {
        return boundingBox;
    }

}
