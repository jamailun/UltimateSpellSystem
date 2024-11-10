package fr.jamailun.ultimatespellsystem.bukkit.runner.nodes.operators;

import fr.jamailun.ultimatespellsystem.api.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.bukkit.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.bukkit.runner.errors.UnreachableRuntimeException;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Duration;
import org.bukkit.Location;
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
            UltimateSpellSystem.logDebug("SUB durations. produced " + ld.sub(rd));
            return ld.sub(rd);
        }
        // Add Locations
        if(left instanceof Location ll && right instanceof Location rl) {
            return ll.clone().subtract(rl);
        }

        throw new UnreachableRuntimeException("Unexpected types : L="+left+", R="+right);
    }

    @Contract(pure = true)
    @Override
    public @NotNull String toString() {
        return leftExpression + " - " + rightExpression;
    }

}
