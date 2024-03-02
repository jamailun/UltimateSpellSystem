package fr.jamailun.ultimatespellsystem.bukkit.entities;

import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

/**
 * A custom entity, created through summoning.
 */
public abstract class CustomEntity {

    /** Attributes. */
    protected final NewEntityAttributes attributes;

    /**
     * Create a new Custom entity.
     * @param attributes the attributes to use.
     */
    public CustomEntity(@NotNull NewEntityAttributes attributes) {
        this.attributes = attributes;
    }

    public abstract Entity getEntity();

    /**
     * Mark the entity for removal.
     */
    public abstract void remove();

    /**
     * Get the current velocity.
     * @return a non-null velocity, in meters/tick.
     */
    public abstract Vector getVelocity();


    /**
     * Set the current velocity.
     * @param velocity  a non-null velocity, in meters/tick.
     */
    public abstract void setVelocity(@NotNull Vector velocity);
}
