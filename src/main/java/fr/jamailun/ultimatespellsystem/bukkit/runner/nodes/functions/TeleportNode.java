package fr.jamailun.ultimatespellsystem.bukkit.runner.nodes.functions;

import fr.jamailun.ultimatespellsystem.bukkit.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.bukkit.runner.RuntimeStatement;
import fr.jamailun.ultimatespellsystem.bukkit.runner.SpellRuntime;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

public class TeleportNode extends RuntimeStatement {

    private final RuntimeExpression entity;
    private final RuntimeExpression target;

    public TeleportNode(RuntimeExpression entity, RuntimeExpression target) {
        this.entity = entity;
        this.target = target;
    }

    @Override
    public void run(SpellRuntime runtime) {
        Entity entity = (Entity) this.entity.evaluate(runtime);
        Object target = this.target.evaluate(runtime);

        if(target instanceof Location location) {
            entity.teleport(location);
        } else if(target instanceof Entity ent) {
            entity.teleport(ent);
        } else {
            throw new RuntimeException("Invalid type for target " + target);
        }
    }

    @Override
    public String toString() {
        return "TELEPORT " + entity + " TO " + target;
    }
}