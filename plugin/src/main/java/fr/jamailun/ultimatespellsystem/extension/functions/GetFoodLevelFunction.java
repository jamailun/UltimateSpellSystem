package fr.jamailun.ultimatespellsystem.extension.functions;

import fr.jamailun.ultimatespellsystem.api.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.api.runner.SpellRuntime;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.functions.FunctionArgument;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.functions.FunctionType;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * get the food-level of a player
 */
public class GetFoodLevelFunction extends AbstractFunction {

    public GetFoodLevelFunction() {
        super(
                "get_food",
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
        LivingEntity target = toLivingEntity("get_food:entity", arguments.getFirst(), runtime);
        if(target instanceof HumanEntity pl)
            return (double) pl.getFoodLevel();
        return 0d;
    }
}
