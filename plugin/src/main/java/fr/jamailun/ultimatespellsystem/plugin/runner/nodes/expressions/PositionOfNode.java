package fr.jamailun.ultimatespellsystem.plugin.runner.nodes.expressions;

import fr.jamailun.ultimatespellsystem.api.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.api.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.api.runner.SpellRuntime;
import fr.jamailun.ultimatespellsystem.api.runner.errors.InvalidTypeException;
import fr.jamailun.ultimatespellsystem.api.entities.SpellEntity;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class PositionOfNode extends RuntimeExpression {

    private final RuntimeExpression entity;
    private final boolean isCollection;

    @Override
    public Object evaluate(@NotNull SpellRuntime runtime) {
        if(isCollection) {
            return runtime.safeEvaluateList(this.entity, Object.class).stream()
                    .map(PositionOfNode::getLocation)
                    .collect(Collectors.toCollection(ArrayList::new));
        }
        Object entityRef = runtime.safeEvaluate(this.entity, Object.class);
        Location location = getLocation(entityRef);
        UltimateSpellSystem.logDebug("PositionOf : " + entityRef + (entityRef==null?"":" = " +location));
        return location;

    }

    private static @Nullable Location getLocation(@Nullable Object object) {
        return switch (object) {
            case null -> null;
            case Entity entity -> entity.getLocation();
            case SpellEntity spellEntity -> spellEntity.getLocation();
            case Location loc -> loc.clone();
            default -> throw new InvalidTypeException("position-of:entity", "entity", object);
        };

    }

    @Override
    public String toString() {
        return "position-of(" + entity + ")";
    }
}
