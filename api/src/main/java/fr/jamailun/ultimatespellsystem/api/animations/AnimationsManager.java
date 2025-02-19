package fr.jamailun.ultimatespellsystem.api.animations;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Manages and run {@link Animation}.
 */
public interface AnimationsManager {

    /**
     * Play a new animation, for all players.
     * @param animation the non-null animation to play.
     */
    default void play(@NotNull Animation animation) {
        play(animation, null);
    }

    /**
     * Play a new animation, for all players.
     * @param animation the non-null animation to play.
     * @param callback a callback to play once the animation is over.
     */
    void play(@NotNull Animation animation, @Nullable Runnable callback);

    /**
     * Stop and purge all currently-played {@link Animation}.
     */
    void purge();

}
