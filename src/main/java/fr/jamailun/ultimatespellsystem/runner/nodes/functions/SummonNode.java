package fr.jamailun.ultimatespellsystem.runner.nodes.functions;

import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Duration;
import fr.jamailun.ultimatespellsystem.entities.SummonAttributes;
import fr.jamailun.ultimatespellsystem.entities.SummonsRegistry;
import fr.jamailun.ultimatespellsystem.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.runner.RuntimeStatement;
import fr.jamailun.ultimatespellsystem.runner.SpellRuntime;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.Collections;
import java.util.Map;

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
        Entity caster = runtime.getCaster();
        Location loc = caster.getLocation();

        // Summon
        Entity entity = SummonsRegistry.getInstance().summon(
                new SummonAttributes(caster, loc, entityType, getProperties(runtime), duration)
        );

        // Set variable if set
        if(optVariableName != null) {
            runtime.variables().set(optVariableName, entity);
        }
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> getProperties(SpellRuntime runtime) {
        if(optProperty != null) {
            // Already checked by type validation !
            return (Map<String, Object>) optProperty.evaluate(runtime);
        }
        return Collections.emptyMap();
    }
}
