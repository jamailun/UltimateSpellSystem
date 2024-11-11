package fr.jamailun.ultimatespellsystem.extension.functions;

import fr.jamailun.ultimatespellsystem.bukkit.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.bukkit.runner.SpellRuntime;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.functions.FunctionArgument;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.functions.FunctionType;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * A ray-cast to find a hit block.
 */
public class DistanceFunction extends AbstractFunction {

    public DistanceFunction() {
        super(
                "distance",
                // Returns a location
                TypePrimitive.NUMBER.asType(),
                // Args :
                // - a : location A
                // - b : location B
                List.of(
                        new FunctionArgument(
                                FunctionType.accept(TypePrimitive.LOCATION, TypePrimitive.ENTITY),
                                "a", false
                        ),
                        new FunctionArgument(
                                FunctionType.accept(TypePrimitive.LOCATION, TypePrimitive.ENTITY),
                                "b", false
                        )
                )
        );
    }

    @Override
    public Object compute(@NotNull List<RuntimeExpression> arguments, @NotNull SpellRuntime runtime) {
        // Location source
        Location a = toLocation("distance:a", arguments.get(0), runtime);
        Location b = toLocation("distance:b", arguments.get(1), runtime);
        return a.distance(b);
    }
}