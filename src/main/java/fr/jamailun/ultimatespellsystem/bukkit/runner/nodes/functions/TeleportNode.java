package fr.jamailun.ultimatespellsystem.bukkit.runner.nodes.functions;

import fr.jamailun.ultimatespellsystem.bukkit.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.bukkit.runner.RuntimeStatement;
import fr.jamailun.ultimatespellsystem.bukkit.runner.SpellRuntime;
import fr.jamailun.ultimatespellsystem.bukkit.spells.SpellEntity;
import org.bukkit.Location;

import java.util.List;

public class TeleportNode extends RuntimeStatement {

    private final RuntimeExpression entity;
    private final RuntimeExpression target;

    public TeleportNode(RuntimeExpression entity, RuntimeExpression target) {
        this.entity = entity;
        this.target = target;
    }

    @Override
    public void run(SpellRuntime runtime) {
        List<SpellEntity> entities = runtime.safeEvaluateAcceptsList(this.entity, SpellEntity.class);
        Object target = this.target.evaluate(runtime);

        Location location;
        if(target instanceof Location loc) {
            location = loc;
        } else if(target instanceof SpellEntity ent) {
            location = ent.getLocation();
        } else {
            throw new RuntimeException("Invalid type for target " + target);
        }

        entities.forEach(e -> e.teleport(location));
    }

    @Override
    public String toString() {
        return "TELEPORT " + entity + " TO " + target;
    }
}
