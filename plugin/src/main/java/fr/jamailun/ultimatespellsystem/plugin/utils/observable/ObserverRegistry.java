package fr.jamailun.ultimatespellsystem.plugin.utils.observable;

import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Abstract observable.
 * @param <T> the type to observe.
 */
public final class ObserverRegistry<T> implements Observable<T> {

    private final Set<Consumer<T>> callbacks = new HashSet<>();

    @Override
    public void registerObserver(@NotNull Consumer<T> callback) {
        callbacks.add(callback);
    }

    /**
     * Call all observers.
     * @param data the data to send.
     */
    public void call(T data) {
        callbacks.forEach(c -> c.accept(data));
    }

}
