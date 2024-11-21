package fr.jamailun.ultimatespellsystem.bukkit.utils;

import fr.jamailun.ultimatespellsystem.api.bukkit.entities.UssEntityType;
import fr.jamailun.ultimatespellsystem.bukkit.providers.ScopeProvider;
import fr.jamailun.ultimatespellsystem.api.bukkit.runner.errors.UnreachableRuntimeException;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Util methods to find entities with a criteria.
 */
public final class EntitiesFinder {

    public static @NotNull Predicate<Entity> findScope(@NotNull Object scope) {
        if(scope instanceof UssEntityType entityType) {
            return (entityType::isOf);
        } else if(scope instanceof String s) {
            Predicate<Entity> predicate = ScopeProvider.instance().find(s);
            return Objects.requireNonNullElse(predicate, x -> true);
        } else {
            throw new UnreachableRuntimeException("Invalid scope type : " + scope);
        }
    }

    public static @NotNull List<Entity> findAllEntities(@NotNull Object scope, @NotNull World world) {
        Predicate<Entity> scopePredicate = findScope(scope);
        return world.getEntities().stream()
                .filter(scopePredicate)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public static @NotNull List<Entity> findEntitiesAround(@NotNull Object scope, @NotNull Location around, double distance) {
        Predicate<Entity> scopePredicate = findScope(scope);
        return around.getWorld().getNearbyEntities(around, distance, distance, distance, scopePredicate).stream()
                .filter(e -> e.getLocation().distance(around) <= distance)
                .collect(Collectors.toCollection(ArrayList::new));
    }

}
