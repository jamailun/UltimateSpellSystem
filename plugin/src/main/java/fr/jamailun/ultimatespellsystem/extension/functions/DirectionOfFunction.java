package fr.jamailun.ultimatespellsystem.extension.functions;

import fr.jamailun.ultimatespellsystem.api.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.api.runner.SpellRuntime;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.functions.FunctionArgument;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.functions.FunctionType;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Direction of an entity
 */
public class DirectionOfFunction extends AbstractFunction {

    public DirectionOfFunction() {
        super(
                "direction_of",
                // Returns "vector"
                TypePrimitive.LOCATION.asType(),
                // Args : the entity to read direction of
                List.of(
                        new FunctionArgument(
                                FunctionType.accept(TypePrimitive.ENTITY),
                                "entity", false
                        )
                )
        );
    }

    @Override
    public Location compute(@NotNull List<RuntimeExpression> arguments, @NotNull SpellRuntime runtime) {
        LivingEntity target = toLivingEntity("direction_of:entity", arguments.getFirst(), runtime);
        if(target == null)
            throw new RuntimeException("Target entity not found for direction_of:entity.");
        return target.getLocation().getDirection().toLocation(target.getWorld());
    }
}
