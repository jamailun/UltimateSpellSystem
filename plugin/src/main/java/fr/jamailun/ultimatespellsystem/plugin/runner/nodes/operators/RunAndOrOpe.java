package fr.jamailun.ultimatespellsystem.plugin.runner.nodes.operators;

import fr.jamailun.ultimatespellsystem.api.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.api.runner.errors.UnreachableRuntimeException;

public final class RunAndOrOpe extends RuntimeBiOperator {

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
