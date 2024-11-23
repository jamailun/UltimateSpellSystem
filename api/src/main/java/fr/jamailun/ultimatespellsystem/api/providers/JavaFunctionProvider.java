package fr.jamailun.ultimatespellsystem.api.providers;

import fr.jamailun.ultimatespellsystem.api.runner.functions.RunnableJavaFunction;
import fr.jamailun.ultimatespellsystem.dsl.registries.FunctionDefinitionsRegistry;
import org.jetbrains.annotations.NotNull;

/**
 * A provider for the {@link RunnableJavaFunction}.
 */
public final class JavaFunctionProvider extends UssProvider<RunnableJavaFunction> {
    private JavaFunctionProvider() {}
    private static final JavaFunctionProvider INSTANCE = new JavaFunctionProvider();
    /** Get the non-null instance. **/
    public static @NotNull JavaFunctionProvider instance() {
        return INSTANCE;
    }

    @Override
    protected void postRegister(@NotNull String key, @NotNull RunnableJavaFunction function) {
        // Also register to the DSL.
        if(!FunctionDefinitionsRegistry.exists(function.getDslDefinition().id()))
            FunctionDefinitionsRegistry.register(function.getDslDefinition());
    }

}
