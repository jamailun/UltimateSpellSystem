package fr.jamailun.ultimatespellsystem.extension.functions;

import fr.jamailun.ultimatespellsystem.api.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.api.runner.SpellRuntime;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.functions.FunctionArgument;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.functions.FunctionType;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Set the aggro of a {@link Mob} to another {@link LivingEntity}.
 */
public class SetAggroFunction extends AbstractFunction {

    public SetAggroFunction() {
        super(
                "set_aggro",
                TypePrimitive.NULL.asType(),
                List.of(
                        new FunctionArgument(
                                FunctionType.accept(TypePrimitive.ENTITY),
                                "entity", false
                        ),
                        new FunctionArgument(
                                FunctionType.accept(TypePrimitive.ENTITY),
                                "target", false
                        )
                )
        );
    }

    @Override
    public Void compute(@NotNull List<RuntimeExpression> arguments, @NotNull SpellRuntime runtime) {
        LivingEntity entity = toLivingEntity("set_aggro:entity", arguments.get(0), runtime);
        if(entity instanceof Mob mob) {
            LivingEntity target = toLivingEntity("set_aggro:target", arguments.get(1), runtime);
            mob.setTarget(target);
        }
        return null;
    }
}
