package fr.jamailun.ultimatespellsystem.extension.functions;

import fr.jamailun.ultimatespellsystem.api.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.api.runner.SpellRuntime;
import fr.jamailun.ultimatespellsystem.api.runner.errors.InvalidTypeException;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.functions.FunctionArgument;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.functions.FunctionType;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Transform a location to a vector.
 */
public class LocationToListFunction extends AbstractFunction {

    public LocationToListFunction() {
        super(
                "loc_to_list",
                // Returns true if success
                TypePrimitive.NUMBER.asType(true),
                // Args : the location to strike
                List.of(
                        new FunctionArgument(
                                FunctionType.accept(TypePrimitive.LOCATION, TypePrimitive.NUMBER),
                                "location", false
                        )
                )
        );
    }

    @Override
    public Object compute(@NotNull List<RuntimeExpression> arguments, @NotNull SpellRuntime runtime) {
        Object arg = arguments.getFirst().evaluate(runtime);
        if(arg instanceof Location loc) {
            return List.of(loc.getX(), loc.getY(), loc.getZ());
        } else if(arg instanceof List<?> list) {
            List<Double> output = new ArrayList<>();
            for(Object obj : list) {
                if(!(obj instanceof Double d))
                    throw new InvalidTypeException("loc_to_list:loc", "got a list with a non-number: " + obj);
                // Add the double.
                output.add(d);
                if(output.size() == 3)
                    break;
            }
            // Fill with 0 if needed
            for(int i = output.size(); i < 3; i++) output.add(0d);
            return output;
        } else if(arg instanceof Double) {
            throw new InvalidTypeException("loc_to_list:loc", "the argument must be a LIST of numbers, not just a single number.");
        }
        throw new InvalidTypeException("loc_to_list:loc", "location/number[]", arg);
    }
}
