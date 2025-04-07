package fr.jamailun.ultimatespellsystem.plugin.runner.nodes.expressions;

import fr.jamailun.ultimatespellsystem.api.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.api.runner.SpellRuntime;
import fr.jamailun.ultimatespellsystem.api.runner.errors.InvalidTypeException;
import fr.jamailun.ultimatespellsystem.api.runner.errors.UnreachableRuntimeException;
import fr.jamailun.ultimatespellsystem.plugin.entities.BukkitSpellEntity;
import fr.jamailun.ultimatespellsystem.api.entities.SpellEntity;
import fr.jamailun.ultimatespellsystem.plugin.utils.EntitiesFinder;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
public class AllAroundNode extends RuntimeExpression {

    private final RuntimeExpression distance, scope, source;
    private final boolean including;

    @Override
    public List<? extends SpellEntity> evaluate(@NotNull SpellRuntime runtime) {
        // Distance
        Double distance = runtime.safeEvaluate(this.distance, Double.class);
        Objects.requireNonNull(distance, "Provided distance for ALL-AROUND expression is null.");

        // Source
        Object source = this.source.evaluate(runtime);
        if(source instanceof List<?> list && list.size() == 1) {
            source = list.getFirst();
        }

        Location location = switch (source) {
            case null -> throw new InvalidTypeException("all-around::source", "entity/location", "NULL");
            case SpellEntity entity -> entity.getLocation();
            case Location loc -> loc;
            default -> throw new UnreachableRuntimeException("Invalid source type : " + source);
        };

        // Scope
        Object scope = this.scope.evaluate(runtime);

        // Game-logic
        List<Entity> list = EntitiesFinder.findEntitiesAround(scope, location, distance);

        // Remove entity around if not including.
        if(!including && source instanceof SpellEntity around) {
            around.getBukkitEntity().ifPresent(list::remove);
        }

        return list.stream()
                .map(BukkitSpellEntity::new)
                .toList();
    }
}
