package fr.jamailun.ultimatespellsystem.extension.functions;

import fr.jamailun.ultimatespellsystem.UssLogger;
import fr.jamailun.ultimatespellsystem.api.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.api.runner.SpellRuntime;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.functions.FunctionArgument;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.functions.FunctionType;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.plugin.runner.nodes.functions.SendEffectNode;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

/**
 * Test if an entity has a potion effect on it.
 */
public class EntityHasEffectFunction extends AbstractFunction {

    public EntityHasEffectFunction() {
        super(
                "entity_has_effect",
                // Returns a boolean
                TypePrimitive.BOOLEAN.asType(),
                // Args : entity, effect, [min level]
                List.of(
                        new FunctionArgument(
                                FunctionType.accept(TypePrimitive.ENTITY),
                                "entity", false
                        ),
                        new FunctionArgument(
                                FunctionType.accept(TypePrimitive.STRING, TypePrimitive.CUSTOM),
                                "effect", false
                        ),
                        new FunctionArgument(
                                FunctionType.accept(TypePrimitive.NUMBER),
                                "level", true
                        )
                )
        );
    }

    @Override
    public Boolean compute(@NotNull List<RuntimeExpression> arguments, @NotNull SpellRuntime runtime) {
        // Entity
        LivingEntity entity = toLivingEntity("entity_has_effect", arguments.getFirst(), runtime);
        if(entity == null) return false;

        // Potion Effect
        String effectRaw = runtime.safeEvaluate(arguments.get(1), String.class);
        if(effectRaw == null) return false;
        PotionEffectType effect = SendEffectNode.convertEffect(effectRaw);
        if(effect == null) {
            UssLogger.logWarning("In entity_has_effect, effect could not be parsed: '" + effectRaw + "'.");
            return false;
        }

        // Amplifier ?
        int level;
        if(arguments.size() > 2) {
            // The +1 is because the argument is on the potion level, and the end-test is on the amplifier.
            level = 1 + Objects.requireNonNullElse(runtime.safeEvaluate(arguments.get(2), Double.class), 0d).intValue();
        } else {
            level = 0;
        }

        return entity.getActivePotionEffects()
                .stream()
                .anyMatch(eff -> Objects.equals(effect, eff.getType()) && eff.getAmplifier() >= level);
    }
}
