package fr.jamailun.ultimatespellsystem.runner.framework.functions;

import fr.jamailun.ultimatespellsystem.api.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.api.runner.SpellRuntime;
import fr.jamailun.ultimatespellsystem.api.runner.functions.RunnableJavaFunction;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.expressions.functions.FunctionArgument;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.type.Type;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.type.TypePrimitive;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PrintFunction extends RunnableJavaFunction {
    public PrintFunction() {
        super(
                "PRINT",
                Type.NULL,
                List.of(FunctionArgument.of(TypePrimitive.STRING))
        );
    }

    @Override
    public Object compute(@NotNull List<RuntimeExpression> arguments, @NotNull SpellRuntime runtime) {
        System.out.println(arguments.getFirst().evaluate(runtime));
        return 0;
    }
}
