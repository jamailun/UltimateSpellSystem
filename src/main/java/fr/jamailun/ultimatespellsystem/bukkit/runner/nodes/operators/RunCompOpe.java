package fr.jamailun.ultimatespellsystem.bukkit.runner.nodes.operators;

import fr.jamailun.ultimatespellsystem.api.bukkit.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.api.bukkit.runner.errors.UnreachableRuntimeException;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Duration;

public final class RunCompOpe extends RuntimeBiOperator {

    private final boolean isEqual, isGreater;

    public RunCompOpe(RuntimeExpression left, RuntimeExpression right, boolean isEqual, boolean isGreater) {
        super(left, right);
        this.isEqual = isEqual;
        this.isGreater = isGreater;
    }

    @Override
    protected Boolean evaluate(Object left, Object right) {
        if(left instanceof Number ld && right instanceof Number rd) {
            return compare(ld.doubleValue(), rd.doubleValue());
        }
        if(left instanceof Duration ld && right instanceof Duration rd) {
            return compare(ld.toSeconds(), rd.toSeconds());
        }
        throw new UnreachableRuntimeException("Unexpected types : L="+left+", R="+right);
    }

    private boolean compare(double left, double right) {
        if(isGreater) {
            return isEqual ? left >= right : left > right;
        }
        return isEqual ? left <= right : left < right;
    }

    @Override
    public String toString() {
        String ope = isGreater?(isEqual ? ">=" : ">"):(isEqual ? "<=" : "<");
        return leftExpression + " " + ope + " " + rightExpression;
    }

}
