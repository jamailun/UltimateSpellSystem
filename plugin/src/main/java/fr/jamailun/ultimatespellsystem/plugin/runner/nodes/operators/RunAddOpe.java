package fr.jamailun.ultimatespellsystem.plugin.runner.nodes.operators;

import fr.jamailun.ultimatespellsystem.api.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.api.runner.errors.UnreachableRuntimeException;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Duration;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class RunAddOpe extends RuntimeBiOperator {

    public RunAddOpe(RuntimeExpression left, RuntimeExpression right) {
        super(left, right);
    }

    private final static DecimalFormat numberFormat = new DecimalFormat("#.##");

    private static String toStringObject(Object object) {
        if(object instanceof Double d) {
            return numberFormat.format(d);
        }
        return Objects.toString(object);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected Object evaluate(Object left, Object right) {
        // Anyone of them is a String
        if(left instanceof String || right instanceof String) {
            return toStringObject(left) + toStringObject(right);
        }

        // If one of them is null, return the other one
        if(left == null) return right;
        if(right == null) return left;

        // Add doubles
        if(left instanceof Number ld && right instanceof Number rd) {
            return ld.doubleValue() + rd.doubleValue();
        }
        // Add durations
        if(left instanceof Duration ld && right instanceof Duration rd) {
            return ld.add(rd);
        }
        // Add Locations adn vectors
        if(left instanceof Location ll && right instanceof Location rl) {
            return ll.clone().add(rl);
        }
        if(left instanceof Location ll && right instanceof List<?> list && list.size() >= 3) {
            if(list.get(0) instanceof Double x && list.get(1) instanceof Double y && list.get(2) instanceof Double z) {
                return ll.clone().add(x, y, z);
            }
        }
        if(left instanceof Location loc && right instanceof Vector vec) {
            return loc.clone().add(vec);
        } else if(right instanceof Location loc && left instanceof Vector vec) {
            return loc.clone().add(vec);
        }
        if(left instanceof Vector rv && right instanceof Vector lv) {
            return rv.subtract(lv);
        }
        // Union Properties
        if(left instanceof Map<?,?> lm && right instanceof Map<?,?> rm) {
            Map<String, Object> union = (Map<String, Object>) new HashMap<>(lm);
            union.putAll( (Map<String, Object>) rm);
            return union;
        }

        throw new UnreachableRuntimeException("Unexpected types : L="+left+", R="+right);
    }

    @Override
    public String toString() {
        return leftExpression + " + " + rightExpression;
    }
}
