package fr.jamailun.ultimatespellsystem.plugin.runner.nodes.operators.list;

import fr.jamailun.ultimatespellsystem.api.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.api.runner.errors.UnreachableRuntimeException;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * "Remove" operation on a collection, by index.
 */
public class RunListRemIndexOpe extends ListOperator {

    public RunListRemIndexOpe(@NotNull RuntimeExpression left, @NotNull RuntimeExpression right) {
        super(left, right);
    }

    @Override
    protected Object evaluateList(@NotNull Collection<Object> collection, Object arg) {
        if(!(arg instanceof Number index))
            throw new UnreachableRuntimeException("Cannot have a right non-number: " + arg);
        collection.remove(index.intValue());
        return arg;
    }

    @Override
    public String toString() {
        return leftExpression + ".remove_idx(" + rightExpression + ")";
    }
}
