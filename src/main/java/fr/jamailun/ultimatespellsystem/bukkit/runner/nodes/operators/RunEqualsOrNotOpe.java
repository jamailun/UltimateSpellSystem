package fr.jamailun.ultimatespellsystem.bukkit.runner.nodes.operators;

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
            return Objects.equals(left, right);
        return ! Objects.equals(left, right);
    }

    @Override
    public String toString() {
        return leftExpression + (isEqual?" == ":" != ") + rightExpression;
    }

}
