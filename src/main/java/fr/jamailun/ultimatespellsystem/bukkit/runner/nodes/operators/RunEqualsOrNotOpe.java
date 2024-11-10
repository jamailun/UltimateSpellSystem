package fr.jamailun.ultimatespellsystem.bukkit.runner.nodes.operators;

import fr.jamailun.ultimatespellsystem.api.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.bukkit.runner.RuntimeExpression;
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
        if(isEqual)
            return equals(left, right);
        return ! equals(left, right);
    }

    private boolean equals(Object left, Object right) {
        if(left == null || right == null) {
            return isEqual && (left == null) == (right == null);
        }
        if(left instanceof Number l && right instanceof Number r) {
            UltimateSpellSystem.logDebug("Comparison (numbers). L="+l+"; R="+r);
            return l.doubleValue() == r.doubleValue();
        }
        UltimateSpellSystem.logDebug("Comparison. L="+left+"|"+left.getClass()+"; R="+right+"|"+right.getClass());
        return Objects.equals(left, right);
    }

    @Contract(pure = true)
    @Override
    public @NotNull String toString() {
        return leftExpression + (isEqual?" == ":" != ") + rightExpression;
    }

}
