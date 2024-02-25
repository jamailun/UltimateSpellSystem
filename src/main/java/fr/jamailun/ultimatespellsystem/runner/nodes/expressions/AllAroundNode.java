package fr.jamailun.ultimatespellsystem.runner.nodes.expressions;

import fr.jamailun.ultimatespellsystem.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.runner.SpellRuntime;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

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
    public List<Entity> evaluate(SpellRuntime runtime) {
        // Distance
        Double distance = runtime.safeEvaluate(this.distance, Double.class);

        // Source
        Object source = this.source.evaluate(runtime);
        Location location;
        if(source instanceof Entity entity) {
            location = entity.getLocation();
        } else if(source instanceof Location loc) {
            location = loc;
        } else {
            throw new RuntimeException("Invalid source type : " + source);
        }

        // Scope
        Object scope = this.scope.evaluate(runtime);
        Predicate<Entity> scopePredicate;
        if(scope instanceof EntityType entityType) {
            scopePredicate = (entity -> entity.getType() == entityType);
        } else if(scope instanceof String s) {
            System.err.println("Unknown scope: '" + s + "'.");
            scopePredicate = (e -> true);
        } else {
            throw new RuntimeException("Invalid scope type : " + scope);
        }

        // Game-logic
        List<Entity> list = new ArrayList<>(location.getWorld().getNearbyEntities(location, distance, distance, distance, scopePredicate));

        // Remove entity around if not including.
        if(!including && source instanceof Entity around) {
            list.remove(around);
        }

        return list;
    }
}
