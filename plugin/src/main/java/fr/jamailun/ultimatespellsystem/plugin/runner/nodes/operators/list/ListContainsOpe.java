package fr.jamailun.ultimatespellsystem.plugin.runner.nodes.operators.list;

import fr.jamailun.ultimatespellsystem.api.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.api.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.api.runner.errors.UnreachableRuntimeException;
import fr.jamailun.ultimatespellsystem.plugin.runner.nodes.operators.RuntimeBiOperator;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A "does list contains element" operation.
 */
public class ListContainsOpe extends RuntimeBiOperator {

    public ListContainsOpe(@NotNull RuntimeExpression left, @NotNull RuntimeExpression right) {
        super(left, right);
    }

    @Override
    protected Object evaluate(Object left, Object right) {
        if(!(left instanceof List<?> list))
            throw new UnreachableRuntimeException("Cannot have a left non-list: " + left + "|" + left.getClass() + ". L = " + leftExpression + "; R = " + rightExpression);
        // Better performance if we hash data.
        Set<Object> hashedList = new HashSet<>(list);

        if(right instanceof Collection<?> others) {
            return hashedList.containsAll(new HashSet<>(others));
        }
        UltimateSpellSystem.logDebug("[List:contains] Contains ? " + list.contains(right));
        return list.contains(right);
    }

    @Override
    public String toString() {
        return leftExpression + ".contains(" + rightExpression + ")";
    }
}
