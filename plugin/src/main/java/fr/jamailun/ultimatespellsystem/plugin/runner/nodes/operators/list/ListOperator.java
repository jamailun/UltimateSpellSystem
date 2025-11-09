package fr.jamailun.ultimatespellsystem.plugin.runner.nodes.operators.list;

import fr.jamailun.ultimatespellsystem.api.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.api.runner.errors.UnreachableRuntimeException;
import fr.jamailun.ultimatespellsystem.plugin.runner.nodes.operators.RuntimeBiOperator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Specialized bi-operator for list.
 */
public abstract class ListOperator extends RuntimeBiOperator {

    public ListOperator(@NotNull RuntimeExpression left, @NotNull RuntimeExpression right) {
        super(left, right);
    }

    /**
     * Evaluate with a list.
     * @param collection list of objects.
     * @param arg argument of the operator.
     * @return something, according to the operator.
     */
    protected abstract Object evaluateList(@NotNull Collection<Object> collection, Object arg);

    @Override
    @SuppressWarnings("unchecked")
    protected final Object evaluate(Object left, Object right) {
        if(!(left instanceof Collection<?> list))
            throw new UnreachableRuntimeException("Cannot have a left non-list: " + left);

        List<Object> listObj = (List<Object>) list;
        return evaluateList(listObj, right);
    }

    /**
     * Transform a single element or a collection (or a {@code null} value into a collection).
     * @param object object to handle.
     * @return a non-null collection.
     */
    protected @NotNull Collection<Object> elementAsCollection(@Nullable Object object) {
        if(object instanceof Collection<?> coll) {
            return new HashSet<>(coll);
        }
        if(object == null)
            return new HashSet<>();
        return Set.of(object);
    }
}
