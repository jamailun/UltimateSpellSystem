package fr.jamailun.ultimatespellsystem.bukkit.runner.nodes.functions;

import fr.jamailun.ultimatespellsystem.api.bukkit.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.api.bukkit.runner.RuntimeStatement;
import fr.jamailun.ultimatespellsystem.api.bukkit.runner.SpellRuntime;
import fr.jamailun.ultimatespellsystem.api.bukkit.runner.errors.InvalidTypeException;
import fr.jamailun.ultimatespellsystem.api.bukkit.runner.errors.UnreachableRuntimeException;
import fr.jamailun.ultimatespellsystem.api.bukkit.entities.SpellEntity;
import lombok.AllArgsConstructor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@AllArgsConstructor
public class TeleportNode extends RuntimeStatement {

    private final RuntimeExpression entity;
    private final RuntimeExpression target;

    @Override
    public void run(@NotNull SpellRuntime runtime) {
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

    private static void teleport(Object object, Location location) {
        if(object instanceof Entity entity) {
            Location target = location.clone().setDirection(entity.getLocation().getDirection());
            entity.teleport(target);
        } else if(object instanceof SpellEntity spellEntity) {
            Location target = location.clone().setDirection(spellEntity.getLocation().getDirection());
            spellEntity.teleport(target);
        } else {
            throw new InvalidTypeException("teleporting entity", "entity", object);
        }
    }

    @Override
    public String toString() {
        return "TELEPORT " + entity + " TO " + target;
    }
}
