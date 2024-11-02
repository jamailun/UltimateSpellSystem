package fr.jamailun.ultimatespellsystem.bukkit.runner.nodes.operators;

import fr.jamailun.ultimatespellsystem.bukkit.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.bukkit.runner.errors.UnreachableRuntimeException;

public final class RunMulDivOpe extends RuntimeBiOperators {

    private final boolean isMultiplication;
    public RunMulDivOpe(RuntimeExpression left, RuntimeExpression right, boolean isMultiplication) {
        super(left, right);
        this.isMultiplication = isMultiplication;
    }

    @Override
    protected Object evaluate(Object left, Object right) {
        // Left and right MUSt be numbers !
        if(left instanceof Number l && right instanceof Number r) {
            return isMultiplication ? (l.doubleValue() * r.doubleValue()) : (l.doubleValue() / r.doubleValue());
        }
        throw new UnreachableRuntimeException("Unexpected types : L="+left+", R="+right);
    }

    @Override
    public String toString() {
        return leftExpression + (isMultiplication ? " * " : " / ") + rightExpression;
    }

}
