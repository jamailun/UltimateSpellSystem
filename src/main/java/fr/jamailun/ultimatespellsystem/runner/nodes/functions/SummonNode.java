package fr.jamailun.ultimatespellsystem.runner.nodes.functions;

import fr.jamailun.ultimatespellsystem.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Duration;
import fr.jamailun.ultimatespellsystem.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.runner.RuntimeStatement;
import fr.jamailun.ultimatespellsystem.runner.SpellRuntime;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

public class SummonNode extends RuntimeStatement {

    private final RuntimeExpression type;
    private final RuntimeExpression duration;
    private final RuntimeExpression optProperty;
    private final String optVariableName;

    public SummonNode(RuntimeExpression type, RuntimeExpression duration, RuntimeExpression optProperty, String optVariableName) {
        this.type = type;
        this.duration = duration;
        this.optProperty = optProperty;
        this.optVariableName = optVariableName;
    }

    @Override
    public void run(SpellRuntime runtime) {
        EntityType entityType = runtime.safeEvaluate(type, EntityType.class);
        Duration duration = runtime.safeEvaluate(this.duration, Duration.class);

        //TODO PROPERTIES !!

        // Summon
        Location loc = runtime.getCaster().getLocation();
        Entity entity = loc.getWorld().spawnEntity(loc, entityType, false);

        // Delete after time
        UltimateSpellSystem.runTaskLater(
                entity::remove,
                duration.toTicks()
        );

        if(optVariableName != null) {
            runtime.variables().set(optVariableName, entity);
        }

    }
}
