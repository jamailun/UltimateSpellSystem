package fr.jamailun.ultimatespellsystem.api.providers;

import fr.jamailun.ultimatespellsystem.dsl.objects.CallbackEvent;
import fr.jamailun.ultimatespellsystem.dsl.registries.CallbackEventRegistry;
import org.jetbrains.annotations.NotNull;

/**
 * Provider for {@link CallbackEvent}.
 */
public final class CallbackEventProvider extends UssProvider<CallbackEvent> {
    private CallbackEventProvider() {}
    private static final CallbackEventProvider INSTANCE = new CallbackEventProvider();

    /**
     * Get the instance.
     * @return the non-null, existing instance.
     */
    public static @NotNull CallbackEventProvider instance() {
        return INSTANCE;
    }

    /**
     * Short method to register a callback.
     * @param callback the callback to register.
     */
    public void registerCallback(@NotNull CallbackEvent callback) {
        super.register(callback, callback.name());
    }

    @Override
    protected void postRegister(@NotNull String key, @NotNull CallbackEvent callback) {
        // Also register to the DSL.
        CallbackEventRegistry.register(callback);
    }
}
