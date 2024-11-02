package fr.jamailun.ultimatespellsystem.bukkit.runner.nodes.functions;

import fr.jamailun.ultimatespellsystem.bukkit.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.bukkit.runner.RuntimeStatement;
import fr.jamailun.ultimatespellsystem.bukkit.runner.SpellRuntime;
import fr.jamailun.ultimatespellsystem.bukkit.runner.errors.InvalidTypeException;
import fr.jamailun.ultimatespellsystem.bukkit.runner.errors.UnreachableRuntimeException;
import fr.jamailun.ultimatespellsystem.bukkit.spells.SpellEntity;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

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
        List<Object> entities = runtime.safeEvaluateAcceptsList(this.entity, Object.class);

        // Target (mono)
        Object target = this.target.evaluate(runtime);
        if(target instanceof List<?> list && list.size() == 1) {
            target = list.get(0);
        }

        Location location;
        if(target instanceof Location loc) {
            location = loc;
        } else if(target instanceof SpellEntity ent) {
            location = ent.getLocation();
        } else {
            throw new UnreachableRuntimeException("Invalid type for target " + target + ": " + target.getClass());
        }

        entities.forEach(e -> teleport(e, location));
    }

    private void teleport(Object object, Location location) {
        if(object instanceof Entity entity) {
            entity.teleport(location);
        } else if(object instanceof SpellEntity spellEntity) {
            spellEntity.teleport(location);
        } else {
            throw new InvalidTypeException("teleporting entity", "entity", object);
        }
    }

    @Override
    public String toString() {
        return "TELEPORT " + entity + " TO " + target;
    }
}
