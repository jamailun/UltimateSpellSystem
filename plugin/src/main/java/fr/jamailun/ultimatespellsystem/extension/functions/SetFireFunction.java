package fr.jamailun.ultimatespellsystem.extension.functions;

import fr.jamailun.ultimatespellsystem.api.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.api.runner.SpellRuntime;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.functions.FunctionArgument;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.functions.FunctionType;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Test if an entity is valid.
 */
public class SetFireFunction extends AbstractFunction {

    public SetFireFunction() {
        super(
                "set_fire",
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
                                "ticks", false
                        )
                )
        );
    }

    @Override
    public Object compute(@NotNull List<RuntimeExpression> arguments, @NotNull SpellRuntime runtime) {
        LivingEntity entity = toLivingEntity("set_fire:entity", arguments.getFirst(), runtime);
        if(entity == null) return false;

        int ticks = toInteger("set_fire:ticks", arguments.get(1), runtime);
        entity.setFireTicks(ticks);

        return true;
    }
}
