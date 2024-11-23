package fr.jamailun.ultimatespellsystem.plugin.runner.nodes.functions;

import fr.jamailun.ultimatespellsystem.api.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.api.entities.UssEntityType;
import fr.jamailun.ultimatespellsystem.api.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.api.runner.RuntimeStatement;
import fr.jamailun.ultimatespellsystem.api.runner.SpellRuntime;
import fr.jamailun.ultimatespellsystem.api.entities.SpellEntity;
import fr.jamailun.ultimatespellsystem.plugin.entities.SummonAttributesImpl;
import fr.jamailun.ultimatespellsystem.api.runner.errors.InvalidTypeException;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Duration;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class SummonNode extends RuntimeStatement {

    private final RuntimeExpression type, source, duration;
    private final RuntimeExpression optProperty;
    private final String optVariableName;

    @Override
    public void run(@NotNull SpellRuntime runtime) {
        UssEntityType entityType = runtime.safeEvaluate(type, UssEntityType.class);
        Duration duration = runtime.safeEvaluate(this.duration, Duration.class);
        LivingEntity caster = runtime.getCaster();
        Location loc = caster.getLocation();
        if(source != null) {
            Object sourceValue = runtime.safeEvaluate(source, Object.class);
            if(sourceValue instanceof Location sourceLoc) {
                loc = sourceLoc;
            } else if(sourceValue instanceof SpellEntity sourceEntity) {
                if(entityType.doesPreferEyesLocation()) {
                    loc = sourceEntity.getEyeLocation();
                } else {
                    loc = sourceEntity.getLocation();
                }
            } else {
                throw new InvalidTypeException("teleport[loc]", "location or entity", sourceValue);
            }
        }

        // Summon
        SpellEntity entity = UltimateSpellSystem.getSummonsManager().summon(
                new SummonAttributesImpl(caster, loc, entityType, getProperties(optProperty, runtime), duration),
                runtime
        );

        UltimateSpellSystem.logDebug("Summon created " + entity);
        // Set variable if set
        if(optVariableName != null) {
            runtime.variables().set(optVariableName, entity);
        }
    }

    @Override
    public @NotNull String toString() {
        return "SummonNode{"+(optProperty==null?"":optProperty+":= ")+type+", for "+duration+" at "+source+"}";
    }
}
