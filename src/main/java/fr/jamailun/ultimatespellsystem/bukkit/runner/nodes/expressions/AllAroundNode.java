package fr.jamailun.ultimatespellsystem.bukkit.runner.nodes.expressions;

import fr.jamailun.ultimatespellsystem.bukkit.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.bukkit.entities.UssEntityType;
import fr.jamailun.ultimatespellsystem.bukkit.providers.ScopeProvider;
import fr.jamailun.ultimatespellsystem.bukkit.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.bukkit.runner.SpellRuntime;
import fr.jamailun.ultimatespellsystem.bukkit.runner.errors.UnreachableRuntimeException;
import fr.jamailun.ultimatespellsystem.bukkit.spells.SpellEntity;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

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
        Predicate<Entity> scopePredicate;
        if(scope instanceof UssEntityType entityType) {
            scopePredicate = (entityType::isOf);
        } else if(scope instanceof String s) {
            scopePredicate = ScopeProvider.instance().find(s);
        } else {
            throw new UnreachableRuntimeException("Invalid scope type : " + scope);
        }

        // Game-logic
        List<Entity> list = new ArrayList<>(location.getWorld().getNearbyEntities(location, distance, distance, distance, scopePredicate));

        // Remove entity around if not including.
        if(!including && source instanceof Entity around) {
            list.remove(around);
        }

        UltimateSpellSystem.logDebug("All around :: " + list);

        return list;
    }
}
