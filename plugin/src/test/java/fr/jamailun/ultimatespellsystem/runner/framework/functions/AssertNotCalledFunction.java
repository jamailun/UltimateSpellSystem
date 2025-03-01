package fr.jamailun.ultimatespellsystem.runner.framework.functions;

import fr.jamailun.ultimatespellsystem.api.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.api.runner.SpellRuntime;
import fr.jamailun.ultimatespellsystem.api.runner.functions.RunnableJavaFunction;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.functions.FunctionArgument;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.functions.FunctionType;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.runner.framework.AssertException;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AssertNotCalledFunction extends RunnableJavaFunction {
    public AssertNotCalledFunction() {
        super(
                "ASSERT_NOT_CALLED",
                TypePrimitive.NULL.asType(),
                List.of(
                        new FunctionArgument(
                                FunctionType.acceptOnlyMono(TypePrimitive.STRING),
                                "message",
                                false
                        )
                )
        );
    }

    @Override
    public Object compute(@NotNull List<RuntimeExpression> arguments, @NotNull SpellRuntime runtime) {
        String msg = arguments.isEmpty() ? "" : runtime.safeEvaluate(arguments.getFirst(), String.class);
        throw new AssertException("Should never be called. " + msg);
    }
}
