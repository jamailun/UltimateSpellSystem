package fr.jamailun.ultimatespellsystem.plugin.runner.nodes.operators;

import fr.jamailun.ultimatespellsystem.api.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.api.runner.errors.UnreachableRuntimeException;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Duration;
import org.bukkit.Location;

public final class RunMulDivOpe extends RuntimeBiOperator {

    private final boolean isMultiplication;
    public RunMulDivOpe(RuntimeExpression left, RuntimeExpression right, boolean isMultiplication) {
        super(left, right);
        this.isMultiplication = isMultiplication;
    }

    @Override
    protected Object evaluate(Object left, Object right) {
        if(left instanceof Duration dl && right instanceof Duration dr) {
            if(isMultiplication)
                throw new UnreachableRuntimeException("Cannot multiply durations together.");
            return dl.div(dr);
        }
        // Right must be number.
        if(right instanceof Number r) {
            double rd = r.doubleValue();
            // Left is a number
            if(left instanceof Number l) {
                return isMultiplication ? (l.doubleValue() * rd) : (l.doubleValue() / rd);
            } else if(left instanceof Duration ld) {
                return isMultiplication ? ld.mul(rd) : ld.div(rd);
            } else if(left instanceof Location loc) {
                return isMultiplication ? loc.clone().multiply(rd) : loc.clone().multiply(1/rd);
            }
        }
        throw new UnreachableRuntimeException("Unexpected types : L="+left+", R="+right);
    }

    @Override
    public String toString() {
        return leftExpression + (isMultiplication ? " * " : " / ") + rightExpression;
    }

}
