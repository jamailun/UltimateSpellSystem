package fr.jamailun.ultimatespellsystem.plugin.utils;

import fr.jamailun.ultimatespellsystem.api.runner.errors.InvalidTypeException;
import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Helper for type analysis.
 */
public final class TypeInterpretation {
    private TypeInterpretation() {}


    /**
     * Extract a vector from a raw object.
     * @param raw an object.
     * @return a non-null vector.
     * @throws InvalidTypeException when could not read type.
     */
    public static @NotNull Vector extractDirection(Object raw) {
        if(raw instanceof Location loc) {
            return loc.toVector();
        }

        if(raw instanceof Vector vec) {
            return vec.clone();
        }

        if(raw instanceof List<?> list) {
            double dx = 0, dy = 0, dz = 0;
            int i = 0;
            for(; i < Math.min(3, list.size()); i++) {
                if(!(list.get(i) instanceof Number num))
                    throw new InvalidTypeException("orb.attributes.direction", "direction.list["+i+"]", list.get(i));
                if(i == 0) dx = num.doubleValue();
                if(i == 1) dy = num.doubleValue();
                if(i == 2) dz = num.doubleValue();
            }
            if(i < 1) dx = 0;
            if(i < 2) dy = dx;
            if(i < 3) dz = dy;
            return new Vector(dx, dy, dz);
        }

        throw new InvalidTypeException("orb.attributes.direction", "direction(location/vector/list)", raw);
    }

}
