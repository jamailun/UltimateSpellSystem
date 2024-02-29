package fr.jamailun.ultimatespellsystem.bukkit.runner.nodes.operators;

import fr.jamailun.ultimatespellsystem.bukkit.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.bukkit.runner.SpellRuntime;

public abstract class RuntimeBiOperators extends RuntimeExpression {

    protected final RuntimeExpression leftExpression;
    protected final RuntimeExpression rightExpression;

    public RuntimeBiOperators(RuntimeExpression left, RuntimeExpression right) {
        this.leftExpression = left;
        this.rightExpression = right;
    }

    @Override
    public final Object evaluate(SpellRuntime runtime) {
        return evaluate(leftExpression.evaluate(runtime), rightExpression.evaluate(runtime));
    }

    protected abstract Object evaluate(Object left, Object right);
}
