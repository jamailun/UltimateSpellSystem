package fr.jamailun.ultimatespellsystem.bukkit.runner.nodes.operators;

import fr.jamailun.ultimatespellsystem.bukkit.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.bukkit.runner.errors.UnreachableRuntimeException;

public final class RunAndOrOpe extends RuntimeBiOperators {

    private final boolean isAnd;
    public RunAndOrOpe(RuntimeExpression left, RuntimeExpression right, boolean isAnd) {
        super(left, right);
        this.isAnd = isAnd;
    }

    @Override
    protected Object evaluate(Object left, Object right) {
        // Left and right MUSt be booleans !
        if(left instanceof Boolean l && right instanceof Boolean r) {
            return isAnd ? (l && r) : (l || r);
        }
        throw new UnreachableRuntimeException("Unexpected types : L="+left+", R="+right);
    }

    @Override
    public String toString() {
        return leftExpression + (isAnd ? " && " : " || ") + rightExpression;
    }

}
