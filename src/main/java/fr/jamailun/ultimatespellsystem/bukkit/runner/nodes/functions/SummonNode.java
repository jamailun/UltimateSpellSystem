package fr.jamailun.ultimatespellsystem.bukkit.runner.nodes.functions;

import fr.jamailun.ultimatespellsystem.bukkit.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Duration;
import fr.jamailun.ultimatespellsystem.bukkit.entities.SummonAttributes;
import fr.jamailun.ultimatespellsystem.bukkit.entities.SummonsRegistry;
import fr.jamailun.ultimatespellsystem.bukkit.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.bukkit.runner.RuntimeStatement;
import fr.jamailun.ultimatespellsystem.bukkit.runner.SpellRuntime;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.Collections;
import java.util.Map;

public class SummonNode extends RuntimeStatement {

    private final RuntimeExpression type, source, duration;
    private final RuntimeExpression optProperty;
    private final String optVariableName;

    public SummonNode(RuntimeExpression type, RuntimeExpression source, RuntimeExpression duration, RuntimeExpression optProperty, String optVariableName) {
        this.type = type;
        this.source = source;
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
        if(source != null) {
            Object sourceValue = source.evaluate(runtime);
            if(sourceValue instanceof Location sourceLoc) {
                loc = sourceLoc;
            } else if(sourceValue instanceof Entity sourceEntity) {
                loc = sourceEntity.getLocation();
            } else {
                throw new RuntimeException("Unknown type for location: " + sourceValue);
            }
        }

        // Summon
        Entity entity = SummonsRegistry.instance().summon(
                new SummonAttributes(caster, loc, entityType, getProperties(runtime), duration)
        );

        UltimateSpellSystem.logDebug("Summon created " + entity);
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
