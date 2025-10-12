package fr.jamailun.ultimatespellsystem.extension.functions;

import fr.jamailun.ultimatespellsystem.api.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.api.runner.SpellRuntime;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.functions.FunctionArgument;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.functions.FunctionType;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;

/**
 * A simple random function.
 */
public class RandIntFunction extends AbstractFunction {

    private static final Random RANDOM = new Random();

    public RandIntFunction() {
        super(
                "rand_int",
                // Returns a numeric value
                TypePrimitive.NUMBER.asType(),
                // Args :
                // - lower bound
                // - upper bound
                List.of(
                        new FunctionArgument(
                                FunctionType.accept(TypePrimitive.NUMBER),
                                "lower_bound", false
                        ),
                        new FunctionArgument(
                                FunctionType.accept(TypePrimitive.NUMBER),
                                "upper_bound", false
                        )
                )
        );
    }

    @Override
    public Integer compute(@NotNull List<RuntimeExpression> arguments, @NotNull SpellRuntime runtime) {
        int a = toInteger("rand:min", arguments.get(0), runtime);
        int b = toInteger("rand:max", arguments.get(1), runtime);
        boolean inv = a > b;
        int lower = inv ? b : a;
        int upper = inv ? a : b;
        return RANDOM.nextInt(upper - lower) + lower;
    }
}
