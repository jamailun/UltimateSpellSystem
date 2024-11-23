package fr.jamailun.ultimatespellsystem.plugin.runner.nodes.functions;

import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Type;
import fr.jamailun.ultimatespellsystem.api.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.api.runner.RuntimeStatement;
import fr.jamailun.ultimatespellsystem.api.runner.SpellRuntime;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

/**
 * A node that can define a variable to the runtime.
 */
@RequiredArgsConstructor
public class DefineNode extends RuntimeStatement {

    private final String varName;
    private final RuntimeExpression expression;
    private final Type type;

    @Override
    public void run(@NotNull SpellRuntime runtime) {
        if(type.is(TypePrimitive.NULL))
            return;

        Class<?> clazz = type.primitive().clazz;
        if(clazz == null)
            throw new RuntimeException("Cannot run a null-typed expression : " + runtime);

        //Object value = runtime.safeEvaluateAcceptsList(expression, clazz);

        runtime.variables().set(varName, expression.evaluate(runtime));
    }

    @Override
    public String toString() {
        return "DEFINE %" + varName + " = " + expression;
    }
}