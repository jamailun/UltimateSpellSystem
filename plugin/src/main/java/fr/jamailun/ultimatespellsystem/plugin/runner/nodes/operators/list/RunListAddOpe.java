package fr.jamailun.ultimatespellsystem.plugin.runner.nodes.operators.list;

import fr.jamailun.ultimatespellsystem.api.runner.RuntimeExpression;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * "Append" operation on a collection.
 */
public class RunListAddOpe extends ListOperator {

    public RunListAddOpe(@NotNull RuntimeExpression left, @NotNull RuntimeExpression right) {
        super(left, right);
    }

    @Override
    protected Object evaluateList(@NotNull Collection<Object> collection, Object arg) {
        // Can add either another collection or a single element.
        Collection<Object> allOthers = elementAsCollection(arg);

        // Append values
        collection.addAll(allOthers);
        return collection;
    }

    @Override
    public String toString() {
        return leftExpression +".add(" + rightExpression + ")";
    }
}
