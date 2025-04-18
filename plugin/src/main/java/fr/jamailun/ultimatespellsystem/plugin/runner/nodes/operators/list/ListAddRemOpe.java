package fr.jamailun.ultimatespellsystem.plugin.runner.nodes.operators.list;

import fr.jamailun.ultimatespellsystem.UssLogger;
import fr.jamailun.ultimatespellsystem.api.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.api.runner.errors.UnreachableRuntimeException;
import fr.jamailun.ultimatespellsystem.plugin.runner.nodes.operators.RuntimeBiOperator;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * An 'append' or 'remove' on list, operator.
 */
public class ListAddRemOpe extends RuntimeBiOperator {

    private final boolean append;

    public ListAddRemOpe(@NotNull RuntimeExpression left, @NotNull RuntimeExpression right, boolean append) {
        super(left, right);
        this.append = append;
    }

    @Override
    protected Object evaluate(Object left, Object right) {
        if(!(left instanceof List<?> list))
            throw new UnreachableRuntimeException("Cannot have a left non-list: " + left);

        // Handle MONO and MULTI cases
        Set<Object> allOthers;
        if(right instanceof Collection<?> coll) {
            allOthers = new HashSet<>(coll);
        } else {
            allOthers = Set.of(right);
        }

        List<Object> listObj = (List<Object>) list;
        if(append) {
            listObj.addAll(allOthers);
            UssLogger.logDebug("[List:add] New list: " + listObj);
        } else {
            listObj.removeAll(allOthers);
            UssLogger.logDebug("[List:rem] New list: " + listObj);
        }
        return listObj;
    }

    @Override
    public String toString() {
        return leftExpression +(append?".add(":".remove(") + rightExpression + ")";
    }
}
