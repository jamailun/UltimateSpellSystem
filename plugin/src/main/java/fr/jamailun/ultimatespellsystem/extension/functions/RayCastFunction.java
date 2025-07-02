package fr.jamailun.ultimatespellsystem.extension.functions;

import fr.jamailun.ultimatespellsystem.api.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.api.runner.SpellRuntime;
import fr.jamailun.ultimatespellsystem.api.runner.errors.InvalidTypeException;
import fr.jamailun.ultimatespellsystem.api.entities.SpellEntity;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.functions.FunctionArgument;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.functions.FunctionType;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * A ray-cast to find a hit block.
 */
public class RayCastFunction extends AbstractFunction {

    public RayCastFunction() {
        super(
                "raycast",
                // Returns a location
                TypePrimitive.LOCATION.asType(),
                // Args :
                // - source : location (or entity) to start the raycast from
                // - dir : ARRAY (vector-like) or NULL (if first param is entity, follow eyes)
                // - max : max travel distance
                List.of(
                        new FunctionArgument(
                                FunctionType.accept(TypePrimitive.LOCATION, TypePrimitive.ENTITY),
                                "source", false
                        ),
                        new FunctionArgument(
                                FunctionType.accept(TypePrimitive.NUMBER, TypePrimitive.NULL, TypePrimitive.LOCATION, TypePrimitive.ENTITY),
                                "direction", false
                        ),
                        new FunctionArgument(
                                FunctionType.accept(TypePrimitive.NUMBER),
                                "max", false
                        )
                )
        );
    }

    @Override
    public Object compute(@NotNull List<RuntimeExpression> arguments, @NotNull SpellRuntime runtime) {
        // Location source
        Location location = toLocation("raycast:location", arguments.get(0), runtime);

        // Direction
        Vector direction;
        Object directionRaw = arguments.get(1).evaluate(runtime);
        if(directionRaw == null) {
            direction = location.getDirection();
        } else if(directionRaw instanceof List<?> list) {
            List<Double> numbers = runtime.safeEvaluateList(arguments.get(1), Double.class);
            if(numbers.size() < 3)
                throw new InvalidTypeException("call:raycast[1]", "Direction (array) size must be at least 3. Got " + numbers.size() + " (" + list + ")");
            direction = new Vector(
                    numbers.get(0),
                    numbers.get(1),
                    numbers.get(2)
            );
        } else if(directionRaw instanceof SpellEntity se) {
            direction = se.getLocation().getDirection();
        } else if(directionRaw instanceof Location loc) {
            direction = loc.getDirection();
        } else {
            throw new InvalidTypeException("call:raycast[1]", "null/direction/entity/loc", directionRaw);
        }

        // Range
        double range = toDouble("raycast:range", arguments.get(2), runtime);

        // Raytrace
        RayTraceResult result = location.getWorld().rayTraceBlocks(location, direction, range, FluidCollisionMode.NEVER, true);
        if(result == null)
            return null;
        return result.getHitPosition().toLocation(location.getWorld());
    }
}
