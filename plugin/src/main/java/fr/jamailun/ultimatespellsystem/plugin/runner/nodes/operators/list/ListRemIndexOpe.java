package fr.jamailun.ultimatespellsystem.plugin.runner.nodes.operators.list;

import fr.jamailun.ultimatespellsystem.api.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.api.runner.errors.UnreachableRuntimeException;
import fr.jamailun.ultimatespellsystem.plugin.runner.nodes.operators.RuntimeBiOperator;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * A "remove at index" operation.
 */
public class ListRemIndexOpe extends RuntimeBiOperator {

    public ListRemIndexOpe(@NotNull RuntimeExpression left, @NotNull RuntimeExpression right) {
        super(left, right);
    }

    @Override
    protected Object evaluate(Object left, Object right) {
        if(!(left instanceof List<?> list))
            throw new UnreachableRuntimeException("Cannot have a left non-list: " + left);
        if(!(right instanceof Integer index))
            throw new UnreachableRuntimeException("Cannot have a right non-number: " + right);
        list.remove(index);
        return list;
    }

    @Override
    public String toString() {
        return leftExpression + ".remove(" + rightExpression + ")";
    }
}
