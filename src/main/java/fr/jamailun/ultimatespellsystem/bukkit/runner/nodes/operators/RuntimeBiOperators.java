package fr.jamailun.ultimatespellsystem.bukkit.runner.nodes.operators;

import fr.jamailun.ultimatespellsystem.bukkit.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.bukkit.runner.SpellRuntime;

import java.util.List;

public abstract class RuntimeBiOperators extends RuntimeExpression {

    protected final RuntimeExpression leftExpression;
    protected final RuntimeExpression rightExpression;

    public RuntimeBiOperators(RuntimeExpression left, RuntimeExpression right) {
        this.leftExpression = left;
        this.rightExpression = right;
    }

    @Override
    public final Object evaluate(SpellRuntime runtime) {
        Object left = leftExpression.evaluate(runtime);
        if(left instanceof List<?> listLeft && listLeft.size() == 1)
            left = listLeft.get(0);

        Object right = leftExpression.evaluate(runtime);
        if(right instanceof List<?> listRight && listRight.size() == 1)
            right = listRight.get(0);

        return evaluate(left, right);
    }

    protected abstract Object evaluate(Object left, Object right);
}
