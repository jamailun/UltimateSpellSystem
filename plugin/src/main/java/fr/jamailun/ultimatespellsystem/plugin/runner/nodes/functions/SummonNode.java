package fr.jamailun.ultimatespellsystem.plugin.runner.nodes.functions;

import fr.jamailun.ultimatespellsystem.UssLogger;
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
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@RequiredArgsConstructor
public class SummonNode extends RuntimeStatement {

    private final RuntimeExpression type, source, duration;
    private final RuntimeExpression optProperty;
    private final String optVariableName;

    @Override
    public void run(@NotNull SpellRuntime runtime) {
        UssEntityType entityType = Objects.requireNonNull(runtime.safeEvaluate(type, UssEntityType.class), "Summon.entity-type cannot be null.");
        Duration duration = Objects.requireNonNull(runtime.safeEvaluate(this.duration, Duration.class), "Summon.duration cannot be null.");
        SpellEntity caster = runtime.getCaster();
        Location location = getSource(runtime, caster, entityType);

        // Summon
        SpellEntity entity = UltimateSpellSystem.getSummonsManager().summon(
                new SummonAttributesImpl(caster, location, entityType, getProperties(optProperty, runtime), duration),
                runtime
        );

        UssLogger.logDebug("Summon created " + entity);
        // Set variable if set
        if(optVariableName != null) {
            runtime.variables().set(optVariableName, entity);
        }
    }

    private @NotNull Location getSource(@NotNull SpellRuntime runtime, @NotNull SpellEntity caster, @NotNull UssEntityType type) {
        if(source == null)
            return caster.getLocation();

        Object sourceValue = runtime.safeEvaluate(source, Object.class);
        return switch(sourceValue) {
            case null -> throw new InvalidTypeException("teleport.location", "the value has not been set!");
            case Location sourceLoc -> sourceLoc;
            case SpellEntity sourceEntity -> type.isProjectileLike() ? sourceEntity.getEyeLocation() : sourceEntity.getLocation();
            default -> throw new InvalidTypeException("teleport.location", "location or entity", sourceValue);
        };
    }

    @Override
    public @NotNull String toString() {
        return "SummonNode{"+(optProperty==null?"":optProperty+":= ")+type+", for "+duration+" at "+source+"}";
    }
}
