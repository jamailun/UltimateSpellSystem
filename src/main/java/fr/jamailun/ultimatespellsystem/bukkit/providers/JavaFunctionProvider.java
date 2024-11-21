package fr.jamailun.ultimatespellsystem.bukkit.providers;

import fr.jamailun.ultimatespellsystem.api.bukkit.runner.functions.RunnableJavaFunction;
import fr.jamailun.ultimatespellsystem.dsl.registries.FunctionDefinitionsRegistry;
import org.jetbrains.annotations.NotNull;

/**
 * A provider for the {@link RunnableJavaFunction}.
 */
public final class JavaFunctionProvider extends UssProvider<RunnableJavaFunction> {
    private static final JavaFunctionProvider INSTANCE = new JavaFunctionProvider();
    /** Get the instance. **/
    public static @NotNull JavaFunctionProvider instance() {
        return INSTANCE;
    }

    @Override
    protected void postRegister(@NotNull String key, @NotNull RunnableJavaFunction function) {
        if(!FunctionDefinitionsRegistry.exists(function.getDslDefinition().id()))
            FunctionDefinitionsRegistry.register(function.getDslDefinition());
    }

}
