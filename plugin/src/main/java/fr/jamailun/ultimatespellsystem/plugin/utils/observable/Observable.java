package fr.jamailun.ultimatespellsystem.plugin.utils.observable;

import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public interface Observable<T> {

    /**
     * Register an observer callback.
     * @param callback the non-null callback to call.
     */
    void registerObserver(@NotNull Consumer<T> callback);

}
