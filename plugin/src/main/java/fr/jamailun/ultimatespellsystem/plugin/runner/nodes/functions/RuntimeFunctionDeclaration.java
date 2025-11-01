package fr.jamailun.ultimatespellsystem.plugin.runner.nodes.functions;

import fr.jamailun.ultimatespellsystem.api.runner.FlowState;
import fr.jamailun.ultimatespellsystem.api.runner.RuntimeStatement;
import fr.jamailun.ultimatespellsystem.api.runner.SpellRuntime;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.statements.FunctionDeclarationStatement;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.type.Type;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class RuntimeFunctionDeclaration {

    private final @NotNull String name;
    private final @NotNull Type type;
    private final @NotNull List<FunctionDeclarationStatement.FunctionParameter> args;
    private final @NotNull List<RuntimeStatement> statements;

    public @Nullable Object execute(@NotNull SpellRuntime runtimeSource, @NotNull List<Object> parameters) {
        SpellRuntime runtime = runtimeSource.makeChild(true);

        // Register parameters as variables in local scope
        for(MatchedParam param : matchParams(parameters)) {
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

        //TODO Check the output type

        return output;
    }

    private record MatchedParam(@NotNull String argName, @Nullable Object value) { }

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
