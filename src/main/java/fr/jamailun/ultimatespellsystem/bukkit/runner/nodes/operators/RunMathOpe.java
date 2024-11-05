package fr.jamailun.ultimatespellsystem.bukkit.runner.nodes.operators;

import fr.jamailun.ultimatespellsystem.bukkit.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.bukkit.runner.errors.UnreachableRuntimeException;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.operators.MonoOperator.MonoOpeType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Not-operator
 */
public final class RunMathOpe extends RuntimeMonoOperator {

    private final MonoOpeType type;

    /**
     * Create a new mono-operator.
     * @param child the child expression.
     * @param type the type of operation.
     */
    public RunMathOpe(@NotNull RuntimeExpression child, @NotNull MonoOpeType type) {
        super(child);
        this.type = type;
    }

    @Override
    protected @NotNull Object evaluate(@NotNull Object value) {
        // A math function only handles numbers.
        if(value instanceof List<?> list) {
            // evaluate for each element.
            List<Object> output = new ArrayList<>();
            for(Object subValue : list) {
                output.add(evaluate(subValue));
            }
            return output;
        }

        // If it's a number, accept it
        if(value instanceof Number number) {
            return type.function.apply(number);
        }

        throw new UnreachableRuntimeException("Unexpected type for "+type+"-operator : " + value + " | " + value.getClass());
    }

    @Override
    public @NotNull String toString() {
        return "not(" + child + ")";
    }
}
