package fr.jamailun.ultimatespellsystem.bukkit.runner.nodes.expressions;

import fr.jamailun.ultimatespellsystem.bukkit.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.bukkit.runner.SpellRuntime;
import org.bukkit.entity.Entity;

public class PositionOfNode extends RuntimeExpression {

    private final RuntimeExpression entity;
    private final boolean isCollection;

    public PositionOfNode(RuntimeExpression entity, boolean isCollection) {
        this.entity = entity;
        this.isCollection = isCollection;
    }

    @Override
    public Object evaluate(SpellRuntime runtime) {
        if(isCollection) {
            return runtime.safeEvaluateList(this.entity, Entity.class);
        } else {
            Entity entity = runtime.safeEvaluate(this.entity, Entity.class);
            return (entity == null) ? null : entity.getLocation();
        }
    }
}
