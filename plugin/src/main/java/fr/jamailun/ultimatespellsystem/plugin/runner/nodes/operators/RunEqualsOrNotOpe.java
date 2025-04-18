package fr.jamailun.ultimatespellsystem.plugin.runner.nodes.operators;

import fr.jamailun.ultimatespellsystem.UssLogger;
import fr.jamailun.ultimatespellsystem.api.runner.RuntimeExpression;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class RunEqualsOrNotOpe extends RuntimeBiOperator {

    private final boolean isEqual;

    public RunEqualsOrNotOpe(RuntimeExpression left, RuntimeExpression right, boolean isEqual) {
        super(left, right);
        this.isEqual = isEqual;
    }

    @Override
    protected Boolean evaluate(Object left, Object right) {
        boolean areEqual = equals(left, right);
        return isEqual == areEqual;
    }

    private boolean equals(Object left, Object right) {
        if(left == null || right == null) {
            return (left == null) == (right == null);
        }
        if(left instanceof Number l && right instanceof Number r) {
            UssLogger.logDebug("Comparison (numbers). L="+l+"; R="+r);
            return l.doubleValue() == r.doubleValue();
        }
        UssLogger.logDebug("Comparison. L="+left+"|"+left.getClass()+"; R="+right+"|"+right.getClass());
        return Objects.equals(left, right);
    }

    @Contract(pure = true)
    @Override
    public @NotNull String toString() {
        return leftExpression + (isEqual?" == ":" != ") + rightExpression;
    }

}
