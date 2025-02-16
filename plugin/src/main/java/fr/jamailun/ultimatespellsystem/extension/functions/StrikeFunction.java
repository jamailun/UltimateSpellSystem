package fr.jamailun.ultimatespellsystem.extension.functions;

import fr.jamailun.ultimatespellsystem.api.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.api.runner.SpellRuntime;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.functions.FunctionArgument;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.functions.FunctionType;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Strike a lightning effect
 */
public class StrikeFunction extends AbstractFunction {

    public StrikeFunction() {
        super(
                "strike",
                // Returns true if success
                TypePrimitive.BOOLEAN.asType(),
                // Args : the location to strike
                List.of(
                        new FunctionArgument(
                                FunctionType.accept(TypePrimitive.ENTITY, TypePrimitive.LOCATION),
                                "target", false
                        )
                )
        );
    }

    @Override
    public Object compute(@NotNull List<RuntimeExpression> arguments, @NotNull SpellRuntime runtime) {
        Location target = toLocation("strike:target", arguments.getFirst(), runtime);
        target.getWorld().strikeLightningEffect(target);
        return true;
    }
}
