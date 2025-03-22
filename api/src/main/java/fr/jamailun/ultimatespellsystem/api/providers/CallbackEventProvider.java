package fr.jamailun.ultimatespellsystem.api.providers;

import fr.jamailun.ultimatespellsystem.api.entities.CallbackAction;
import fr.jamailun.ultimatespellsystem.dsl.objects.CallbackEvent;
import fr.jamailun.ultimatespellsystem.dsl.registries.CallbackEventRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * Provider for {@link CallbackAction}.
 */
public final class CallbackEventProvider extends UssProvider<CallbackAction<?,?>> {
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
    public void registerCallback(@NotNull CallbackAction<?,?> callback) {
        super.register(callback, callback.getDslDefinition().name());
    }

    @Override
    protected void postRegister(@NotNull String key, @NotNull CallbackAction<?,?> callback) {
        // Also register to the DSL.
        CallbackEventRegistry.register(callback.getDslDefinition());
    }

    /**
     * Find a callback action using the DSL definition.
     * @param event the event type.
     * @return an optional of the action.
     */
    public @NotNull Optional<CallbackAction<?,?>> find(@NotNull CallbackEvent event) {
        return findOptional(event.name());
    }
}
