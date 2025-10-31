package fr.jamailun.ultimatespellsystem.plugin.runner.structs;

import fr.jamailun.ultimatespellsystem.UssLogger;
import fr.jamailun.ultimatespellsystem.dsl2.library.StructDefinition;
import fr.jamailun.ultimatespellsystem.dsl2.library.structs.ConsoleStruct;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.expressions.functions.FunctionArgument;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.dsl2.registries.ObjectsDefinitionRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * Definition for the 'console' object.
 */
public class ConsoleDefinition extends AbstractStructDefinition<Void> {

    private static ConsoleDefinition FLY_INSTANCE = null;
    public static @NotNull ConsoleDefinition get() {
        if(FLY_INSTANCE == null)
            FLY_INSTANCE = new ConsoleDefinition();
        return FLY_INSTANCE;
    }

    public ConsoleDefinition() {
        super(ConsoleStruct.NAME);
        // Functions
        registerSend("send", UssLogger::logInfo);
        registerSend("debug", UssLogger::logDebug);
        registerSend("info", UssLogger::logInfo);
        registerSend("warning", UssLogger::logWarning);
        registerSend("error", UssLogger::logError);
    }

    private void registerSend(String name, Consumer<String> method) {
        registerNullFunc(
                name,
                (x, args) -> method.accept((String) args.getFirst()),
                FunctionArgument.of(TypePrimitive.STRING)
        );
    }

    @Override
    protected @NotNull StructDefinition computeDsl() {
        return Objects.requireNonNull(ObjectsDefinitionRegistry.getDefaultStruct(structName), "Console struct cannot be found in defaults.");
    }
}
