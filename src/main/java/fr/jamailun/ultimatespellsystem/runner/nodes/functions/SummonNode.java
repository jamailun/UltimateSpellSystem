package fr.jamailun.ultimatespellsystem.runner.nodes.functions;

import fr.jamailun.ultimatespellsystem.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Duration;
import fr.jamailun.ultimatespellsystem.extensible.SummonPropertiesExtension;
import fr.jamailun.ultimatespellsystem.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.runner.RuntimeStatement;
import fr.jamailun.ultimatespellsystem.runner.SpellRuntime;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

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
    @SuppressWarnings("unchecked")
    public void run(SpellRuntime runtime) {
        EntityType entityType = runtime.safeEvaluate(type, EntityType.class);
        Duration duration = runtime.safeEvaluate(this.duration, Duration.class);

        // Summon
        Location loc = runtime.getCaster().getLocation();
        Entity entity = loc.getWorld().spawnEntity(loc, entityType, false);

        // Properties
        if(optProperty != null && entity instanceof LivingEntity livingEntity) {
            // Already checked by type validation !
            Map<String, Object> map = (Map<String, Object>) optProperty.evaluate(runtime);
            for(String key : map.keySet()) {
                SummonPropertiesExtension.SummonProperty prop = SummonPropertiesExtension.instance().getApplier(key);
                if(prop != null)
                    prop.accept(livingEntity, map.get(key));
            }
        }

        // Delete after time
        UltimateSpellSystem.runTaskLater(
                entity::remove,
                duration.toTicks()
        );

        // Set variable if set
        if(optVariableName != null) {
            runtime.variables().set(optVariableName, entity);
        }
    }
}
