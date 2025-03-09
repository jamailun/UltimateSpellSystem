package fr.jamailun.ultimatespellsystem.dsl.registries;

import fr.jamailun.ultimatespellsystem.dsl.objects.CallbackEvent;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * Register new callback handling.
 * @see #register(CallbackEvent)
 */
public final class CallbackEventRegistry {
    private CallbackEventRegistry() {}

    private static final Map<String, CallbackEvent> CALLBACKS = new HashMap<>();

    /**
     * Register a callback.
     * @param callback a valid callback.
     */
    public static void register(@NotNull CallbackEvent callback) {
        CALLBACKS.put(prepare(callback.name()), callback);
    }

    /**
     * Find a callback.
     * @param callbackName name of the callback.
     * @return null if callback name does not exist.
     */
    public static @Nullable CallbackEvent get(@NotNull String callbackName) {
        return CALLBACKS.get(prepare(callbackName));
    }

    /**
     * Test if a callback has been registered.
     * @param callbackName the callback name to test.
     * @return true if callback exists.
     */
    public static boolean exists(@NotNull String callbackName) {
        return CALLBACKS.containsKey(prepare(callbackName));
    }

    /**
     * Remove a callback name.
     * @param callbackName callback name to remove.
     */
    public static void unregister(@NotNull String callbackName) {
        CALLBACKS.remove(prepare(callbackName));
    }

    @Contract(pure = true)
    private static @NotNull String prepare(@NotNull String input) {
        return input.toLowerCase();
    }

}
