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
 * Set food level of a player.
 */
public class SetFoodLevelFunction extends AbstractFunction {

    public SetFoodLevelFunction() {
        super(
                "set_food",
                // Returns true if success
                TypePrimitive.BOOLEAN.asType(),
                // Args : the entity to set on fire
                List.of(
                        new FunctionArgument(
                                FunctionType.accept(TypePrimitive.ENTITY),
                                "entity", false
                        ),
                        new FunctionArgument(
                                FunctionType.accept(TypePrimitive.NUMBER),
                                "amount", false
                        )
                )
        );
    }

    @Override
    public Object compute(@NotNull List<RuntimeExpression> arguments, @NotNull SpellRuntime runtime) {
        LivingEntity entity = toLivingEntity("set_food:entity", arguments.getFirst(), runtime);
        if(!(entity instanceof HumanEntity pl)) return false;
        int amount = toInteger("set_food:amount", arguments.get(1), runtime);

        pl.setFoodLevel(amount);
        return true;
    }
}
