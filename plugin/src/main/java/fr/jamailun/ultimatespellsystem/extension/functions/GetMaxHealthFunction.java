package fr.jamailun.ultimatespellsystem.extension.functions;

import fr.jamailun.ultimatespellsystem.api.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.api.runner.SpellRuntime;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.functions.FunctionArgument;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.functions.FunctionType;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

/**
 * Get the maximum health of a bukkit entity.
 */
public class GetMaxHealthFunction extends AbstractFunction {

    public GetMaxHealthFunction() {
        super(
                "get_max_health",
                // Returns the health value
                TypePrimitive.NUMBER.asType(),
                // Args : the entity to get health from
                List.of(
                        new FunctionArgument(
                                FunctionType.accept(TypePrimitive.ENTITY),
                                "entity", false
                        )
                )
        );
    }

    @Override
    public Double compute(@NotNull List<RuntimeExpression> arguments, @NotNull SpellRuntime runtime) {
        LivingEntity target = toLivingEntity("get_max_health:entity", arguments.getFirst(), runtime);
        if(target == null)
            return 0d;
        return Optional.ofNullable(target.getAttribute(Attribute.GENERIC_MAX_HEALTH))
                .map(AttributeInstance::getValue)
                .orElse(0d);
    }
}
