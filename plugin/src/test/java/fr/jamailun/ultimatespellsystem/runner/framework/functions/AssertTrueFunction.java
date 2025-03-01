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

public class AssertTrueFunction extends RunnableJavaFunction {
    public AssertTrueFunction() {
        super(
                "ASSERT_TRUE",
                TypePrimitive.NULL.asType(),
                List.of(
                        new FunctionArgument(
                                FunctionType.acceptOnlyMono(TypePrimitive.BOOLEAN),
                                "value",
                                true
                        ),
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
        Boolean result = runtime.safeEvaluate(arguments.getFirst(), Boolean.class);
        if(result == null || !result) {
            String msg = arguments.size() == 2 ? runtime.safeEvaluate(arguments.get(1), String.class) : "";
            throw new AssertException("Did not pass assertion. " + msg);
        }
        return 0;
    }
}
