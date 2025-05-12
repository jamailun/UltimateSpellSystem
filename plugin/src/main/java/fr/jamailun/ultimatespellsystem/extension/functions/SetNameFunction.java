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
 * Set the custom name of an entity.
 */
public class SetNameFunction extends AbstractFunction {

    public SetNameFunction() {
        super(
                "set_name",
                TypePrimitive.NULL.asType(),
                List.of(
                        new FunctionArgument(
                                FunctionType.accept(TypePrimitive.ENTITY),
                                "entity", false
                        ),
                        new FunctionArgument(
                                FunctionType.accept(TypePrimitive.STRING),
                                "name", false
                        )
                )
        );
    }

    @Override
    public Void compute(@NotNull List<RuntimeExpression> arguments, @NotNull SpellRuntime runtime) {
        LivingEntity entity = toLivingEntity("v:entity", arguments.get(0), runtime);
        String value = runtime.safeEvaluate(arguments.get(1), String.class);
        entity.setCustomName(value);
        return null;
    }
}
