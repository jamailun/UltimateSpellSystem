package fr.jamailun.ultimatespellsystem.plugin.runner.nodes.operators.list;

import fr.jamailun.ultimatespellsystem.api.runner.RuntimeExpression;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Test if a collection contains an element.
 */
public class RunListContainsOpe extends ListOperator {

    public RunListContainsOpe(@NotNull RuntimeExpression left, @NotNull RuntimeExpression right) {
        super(left, right);
    }

    @Override
    protected Object evaluateList(@NotNull Collection<Object> collection, Object arg) {
        Set<Object> hashedList = new HashSet<>(collection);

        if(arg instanceof Collection<?> others) {
            return hashedList.containsAll(new HashSet<>(others));
        }
        return hashedList.contains(arg);
    }

    @Override
    public String toString() {
        return leftExpression + ".contains(" + rightExpression + ")";
    }
}
