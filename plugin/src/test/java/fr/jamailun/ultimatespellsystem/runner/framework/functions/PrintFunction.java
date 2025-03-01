package fr.jamailun.ultimatespellsystem.runner.framework.functions;

import fr.jamailun.ultimatespellsystem.api.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.api.runner.SpellRuntime;
import fr.jamailun.ultimatespellsystem.api.runner.functions.RunnableJavaFunction;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.functions.FunctionArgument;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.functions.FunctionType;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PrintFunction extends RunnableJavaFunction {
    public PrintFunction() {
        super(
                "PRINT",
                TypePrimitive.NULL.asType(),
                List.of(
                        new FunctionArgument(
                                FunctionType.acceptOnlyMono(TypePrimitive.STRING),
                                "value",
                                true
                        )
                )
        );
    }

    @Override
    public Object compute(@NotNull List<RuntimeExpression> arguments, @NotNull SpellRuntime runtime) {
        System.out.println(arguments.getFirst().evaluate(runtime));
        return 0;
    }
}
