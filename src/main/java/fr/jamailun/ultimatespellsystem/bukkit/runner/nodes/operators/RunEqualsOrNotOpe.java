package fr.jamailun.ultimatespellsystem.bukkit.runner.nodes.operators;

import fr.jamailun.ultimatespellsystem.bukkit.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.bukkit.runner.RuntimeExpression;

import java.util.Objects;

public final class RunEqualsOrNotOpe extends RuntimeBiOperators {

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
        if(left instanceof Number l && right instanceof Number r) {
            UltimateSpellSystem.logDebug("Comparison (numbers). L="+l+"; R="+r);
            return l.doubleValue() == r.doubleValue();
        }
        UltimateSpellSystem.logDebug("Comparison. L="+left+"|"+left.getClass()+"; R="+right+"|"+right.getClass());
        return Objects.equals(left, right);
    }

    @Override
    public String toString() {
        return leftExpression + (isEqual?" == ":" != ") + rightExpression;
    }

}
