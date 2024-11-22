package fr.jamailun.ultimatespellsystem.api.bukkit.entities;

import org.bukkit.entity.Entity;

import java.util.function.Predicate;

/**
 * Can scope entity. Equivalent to a {@code Predicate<Entity>}.
 */
public interface EntityScope extends Predicate<Entity> {
}
