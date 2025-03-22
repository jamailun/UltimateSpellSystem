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
public class RandFunction extends AbstractFunction {

    private static final Random RANDOM = new Random();

    public RandFunction() {
        super(
                "rand",
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
    public Object compute(@NotNull List<RuntimeExpression> arguments, @NotNull SpellRuntime runtime) {
        double a = runtime.safeEvaluate(arguments.get(0), Double.class);
        double b = runtime.safeEvaluate(arguments.get(1), Double.class);
        boolean inv = a > b;
        double lower = inv ? b : a;
        double upper = inv ? a : b;
        return RANDOM.nextDouble(upper - lower) + lower;
    }
}
