package fr.jamailun.ultimatespellsystem.extension.functions;

import fr.jamailun.ultimatespellsystem.UssLogger;
import fr.jamailun.ultimatespellsystem.api.providers.JavaFunctionProvider;
import fr.jamailun.ultimatespellsystem.api.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.api.runner.SpellRuntime;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.functions.FunctionArgument;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.functions.FunctionType;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public abstract class LogFunctions extends AbstractFunction {

    private final Consumer<String> function;
    protected LogFunctions(String id, Consumer<String> function) {
        super(id, TypePrimitive.NULL.asType(), List.of(new FunctionArgument(FunctionType.accept(TypePrimitive.STRING), "str", false)));
        this.function = function;
    }
    @Override
    public final Void compute(@NotNull List<RuntimeExpression> arguments, @NotNull SpellRuntime runtime) {
        function.accept(Objects.toString(arguments.getFirst().evaluate(runtime)));
        return null;
    }

    public static void register() {
        JavaFunctionProvider.instance().registerFunction(new LogInfoFunction());
        JavaFunctionProvider.instance().registerFunction(new LogWarnFunction());
        JavaFunctionProvider.instance().registerFunction(new LogDebugFunction());
        JavaFunctionProvider.instance().registerFunction(new LogErrorFunction());
    }

    public static class LogInfoFunction extends LogFunctions {
        public LogInfoFunction() {
            super("INFO", UssLogger::logInfo);
        }
    }
    public static class LogWarnFunction extends LogFunctions {
        public LogWarnFunction() {
            super("WARN", UssLogger::logWarning);
        }
    }
    public static class LogDebugFunction extends LogFunctions {
        public LogDebugFunction() {
            super("DEBUG", UssLogger::logDebug);
        }
    }
    public static class LogErrorFunction extends LogFunctions {
        public LogErrorFunction() {
            super("ERROR", UssLogger::logError);
        }
    }

}
