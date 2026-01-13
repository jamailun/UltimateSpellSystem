package fr.jamailun.ultimatespellsystem.plugin.runner.nodes.functions;

import fr.jamailun.ultimatespellsystem.api.runner.FlowState;
import fr.jamailun.ultimatespellsystem.api.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.api.runner.RuntimeStatement;
import fr.jamailun.ultimatespellsystem.api.runner.SpellRuntime;
import fr.jamailun.ultimatespellsystem.api.runner.functions.GlobalFunction;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.expressions.functions.FunctionSignature;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.statements.FunctionDeclarationStatement;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.type.Type;
import fr.jamailun.ultimatespellsystem.dsl2.tokenization.TokenPosition;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class RuntimeFunctionDeclaration implements GlobalFunction {

    private final @NotNull String name;
    private final @NotNull Type type;
    private final @NotNull List<FunctionDeclarationStatement.FunctionParameter> args;
    private final @NotNull List<RuntimeStatement> statements;

    @Override
    public @NotNull FunctionSignature getSignature() {
        return new FunctionSignature(name, args.stream().map(fa -> Type.ofAny(fa.type())).toList());
    }

    @Override
    public @Nullable Object call(@NotNull TokenPosition pos, @NotNull List<RuntimeExpression> arguments, @NotNull SpellRuntime runtimeParent) {
        SpellRuntime runtime = runtimeParent.makeChild(true);

        // Handle params
        for(MatchedParam param : matchParams(runtime, arguments)) {
            runtime.variables().set(param.argName(), param.value());
        }

        // Execute statements
        Object output = null;
        for(RuntimeStatement statement : statements) {
            statement.run(runtime);

            // 'Return' was used
            if(runtime.getFlowState() == FlowState.STOPPED) {
                output = runtime.getReturnedValue();
                break;
            }
        }

        //TODO match output type?

        return output;
    }

    private record MatchedParam(@NotNull String argName, @Nullable Object value) { }

    private @NotNull List<MatchedParam> matchParams(@NotNull SpellRuntime runtime, List<RuntimeExpression> args) {
        return matchParams(args.stream().map(a -> a.evaluate(runtime)).toList());
    }
    private @NotNull List<MatchedParam> matchParams(@NotNull List<Object> params) {
        List<MatchedParam> output = new ArrayList<>(args.size());
        for(int i = 0; i < args.size(); i++) {
            FunctionDeclarationStatement.FunctionParameter arg = args.get(i);

            // Le paramètre N est manquant.
            if(params.size() <= i) {
                throw new RuntimeException("Missing args. f="+name+", args=" + params + ", but expected " + args);
            }

            Object value = params.get(i);
            //TODO match type

            output.add(new MatchedParam(arg.name(), value));
        }
        return output;
    }

}
