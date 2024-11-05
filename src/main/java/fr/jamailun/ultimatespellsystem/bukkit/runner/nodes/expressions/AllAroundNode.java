package fr.jamailun.ultimatespellsystem.bukkit.runner.nodes.expressions;

import fr.jamailun.ultimatespellsystem.bukkit.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.bukkit.runner.SpellRuntime;
import fr.jamailun.ultimatespellsystem.bukkit.runner.errors.UnreachableRuntimeException;
import fr.jamailun.ultimatespellsystem.bukkit.spells.BukkitSpellEntity;
import fr.jamailun.ultimatespellsystem.bukkit.spells.SpellEntity;
import fr.jamailun.ultimatespellsystem.bukkit.utils.EntitiesFinder;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AllAroundNode extends RuntimeExpression {

    private final RuntimeExpression distance, scope, source;
    private final boolean including;

    public AllAroundNode(RuntimeExpression distance, RuntimeExpression scope, RuntimeExpression source, boolean including) {
        this.distance = distance;
        this.scope = scope;
        this.source = source;
        this.including = including;
    }

    @Override
    public List<? extends SpellEntity> evaluate(@NotNull SpellRuntime runtime) {
        // Distance
        Double distance = runtime.safeEvaluate(this.distance, Double.class);

        // Source
        Object source = this.source.evaluate(runtime);
        if(source instanceof List<?> list && list.size() == 1) {
            source = list.get(0);
        }

        Location location;
        if(source instanceof SpellEntity entity) {
            location = entity.getLocation();
        } else if(source instanceof Location loc) {
            location = loc;
        } else {
            throw new UnreachableRuntimeException("Invalid source type : " + source);
        }

        // Scope
        Object scope = this.scope.evaluate(runtime);

        // Game-logic
        List<Entity> list = EntitiesFinder.findEntitiesAround(scope, location, distance);

        // Remove entity around if not including.
        if(!including && source instanceof Entity around) {
            list.remove(around);
        }

        return list.stream()
                .map(BukkitSpellEntity::new)
                .toList();
    }
}
