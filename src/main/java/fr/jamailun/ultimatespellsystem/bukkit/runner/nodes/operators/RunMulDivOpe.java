package fr.jamailun.ultimatespellsystem.bukkit.runner.nodes.operators;

import fr.jamailun.ultimatespellsystem.bukkit.runner.RuntimeExpression;

public final class RunMulDivOpe extends RuntimeBiOperators {

    private final boolean isMultiplication;
    public RunMulDivOpe(RuntimeExpression left, RuntimeExpression right, boolean isMultiplication) {
        super(left, right);
        this.isMultiplication = isMultiplication;
    }

    @Override
    protected Object evaluate(Object left, Object right) {
        // Left and right MUSt be numbers !
        if(left instanceof Double l && right instanceof Double r) {
            return isMultiplication ? (l * r) : (l / r);
        }
        throw new RuntimeException("Unexpected types : L="+left+", R="+right);
    }

    @Override
    public String toString() {
        return leftExpression + (isMultiplication ? " * " : " / ") + rightExpression;
    }

}
