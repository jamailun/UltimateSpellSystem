package fr.jamailun.ultimatespellsystem.plugin.runner.structs;

import fr.jamailun.ultimatespellsystem.UssLogger;
import fr.jamailun.ultimatespellsystem.api.runner.structs.Struct;
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
public class ConsoleDefinition extends AbstractStructDefinition<Object> {

    static final ConsoleDefinition INSTANCE = new ConsoleDefinition();

    public ConsoleDefinition() {
        super(ConsoleStruct.NAME);
        // Functions
        registerSend("send", UssLogger::logInfo);
        registerSend("debug", UssLogger::logDebug);
        registerSend("info", UssLogger::logInfo);
        registerSend("warning", UssLogger::logWarning);
        registerSend("error", UssLogger::logError);
    }

    @Override
    public @NotNull Struct instantiate(Object ignored) {
        return new ConsoleInstance();
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
        return Objects.requireNonNull(ObjectsDefinitionRegistry.getDefaultStruct(getName()), "Console struct cannot be found in defaults.");
    }
}
