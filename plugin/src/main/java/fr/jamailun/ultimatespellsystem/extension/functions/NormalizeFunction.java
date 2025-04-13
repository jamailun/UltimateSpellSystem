package fr.jamailun.ultimatespellsystem.extension.functions;

import fr.jamailun.ultimatespellsystem.api.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.api.runner.SpellRuntime;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.functions.FunctionArgument;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.functions.FunctionType;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.plugin.utils.TypeInterpretation;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Normalize a vector.
 */
public class NormalizeFunction extends AbstractFunction {

    public NormalizeFunction() {
        super(
                "normalize",
                // Returns nothing
                TypePrimitive.LOCATION.asType(),
                // Args :
                // location-vector to normalize
                List.of(
                        new FunctionArgument(
                                FunctionType.accept(TypePrimitive.LOCATION),
                                "vector", false
                        )
                )
        );
    }

    @Override
    public Object compute(@NotNull List<RuntimeExpression> arguments, @NotNull SpellRuntime runtime) {
      Object vector = arguments.getFirst().evaluate(runtime);
      return TypeInterpretation.extractDirection(vector).normalize();
    }
}
