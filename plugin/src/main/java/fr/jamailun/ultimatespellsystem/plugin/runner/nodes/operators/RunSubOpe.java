package fr.jamailun.ultimatespellsystem.plugin.runner.nodes.operators;

import fr.jamailun.ultimatespellsystem.api.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.api.runner.errors.UnreachableRuntimeException;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Duration;
import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class RunSubOpe extends RuntimeBiOperator {

    public RunSubOpe(RuntimeExpression left, RuntimeExpression right) {
        super(left, right);
    }

    @Override
    protected Object evaluate(Object left, Object right) {
        // Anyone of them is a String
        if(left instanceof String || right instanceof String) {
            return Objects.toString(left) + right;
        }

        // If one of them is null, return the other one
        if(left == null) return right;
        if(right == null) return left;

        // Add doubles
        if(left instanceof Number ld && right instanceof Number rd) {
            return ld.doubleValue() - rd.doubleValue();
        }
        // Add durations
        if(left instanceof Duration ld && right instanceof Duration rd) {
            return ld.sub(rd);
        }
        // Add Locations
        if(left instanceof Location loc) {
            if(right instanceof Location other)
                return loc.clone().subtract(other);
            if(right instanceof Vector other)
                return loc.clone().subtract(other);
        }
        if(left instanceof Vector vector) {
            if(right instanceof Vector other)
                return vector.clone().subtract(other);
            if(right instanceof Location other)
                return vector.clone().subtract(other.toVector());
        }

        throw new UnreachableRuntimeException("Unexpected types : L="+left+", R="+right);
    }

    @Contract(pure = true)
    @Override
    public @NotNull String toString() {
        return leftExpression + " - " + rightExpression;
    }

}
