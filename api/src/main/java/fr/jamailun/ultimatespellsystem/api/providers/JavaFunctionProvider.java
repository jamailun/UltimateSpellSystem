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
    /**
     * Get the non-null instance.
     * @return singleton instance.
     **/
    public static @NotNull JavaFunctionProvider instance() {
        return INSTANCE;
    }

    /**
     * A way to register a function, without having to repeat the function ID.
     * @param newFunction the non-null fonction to register.
     * @param nameVariants optional array of alternatives to the name.
     */
    public void registerFunction(@NotNull RunnableJavaFunction newFunction, String @NotNull ... nameVariants) {
        super.register(newFunction, newFunction.getId(), nameVariants);
    }

    @Override
    protected void postRegister(@NotNull String key, @NotNull RunnableJavaFunction function) {
        // Also register to the DSL.
        FunctionDefinitionsRegistry.register(key, function.getDslDefinition());
    }

    @Override
    protected @NotNull String prepare(@NotNull String key) {
        // Function are sensible to casing !
        return key.replace(' ', '_');
    }

}
