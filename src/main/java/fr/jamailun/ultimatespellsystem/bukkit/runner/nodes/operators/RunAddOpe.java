package fr.jamailun.ultimatespellsystem.bukkit.runner.nodes.operators;

import fr.jamailun.ultimatespellsystem.bukkit.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Duration;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class RunAddOpe extends RuntimeBiOperators {

    public RunAddOpe(RuntimeExpression left, RuntimeExpression right) {
        super(left, right);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected Object evaluate(Object left, Object right) {
        // Anyone of them is a String
        if(left instanceof String || right instanceof String) {
            return Objects.toString(left) + right;
        }

        // If one of them is null, return the other one
        if(left == null) return right;
        if(right == null) return left;

        // Add doubles
        if(left instanceof Double ld && right instanceof Double rd) {
            return ld + rd;
        }
        // Add durations
        if(left instanceof Duration ld && right instanceof Duration rd) {
            return ld.add(rd);
        }
        // Add Locations
        if(left instanceof Location ll && right instanceof Location rl) {
            return ll.clone().add(rl);
        }
        // Union Properties
        if(left instanceof Map<?,?> lm && right instanceof Map<?,?> rm) {
            Map<String, Object> union = (Map<String, Object>) new HashMap<>(lm);
            union.putAll( (Map<String, Object>) rm);
            return union;
        }

        throw new RuntimeException("Unexpected types : L="+left+", R="+right);
    }

    @Override
    public String toString() {
        return leftExpression + " + " + rightExpression;
    }
}
