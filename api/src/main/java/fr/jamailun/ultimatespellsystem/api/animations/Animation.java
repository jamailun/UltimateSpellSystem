package fr.jamailun.ultimatespellsystem.api.animations;

import lombok.AccessLevel;
import lombok.Getter;

import java.util.Random;

/**
 * An animation can be played using the {@link AnimationsManager}.
 */
public abstract class Animation {

    @Getter(AccessLevel.PROTECTED) private int ticks = 0;

    /** Random instance, to generate random numbers */
    protected final Random random = new Random();

    /** Create a new instance. */
    protected Animation() {}

    /**
     * Get the duration of this animation.
     * @return a non-negative long.
     */
    protected abstract long getDuration();

    /**
     * Called when the animation starts.
     */
    protected abstract void onStart();

    /**
     * Tick the animation.
     * Should <b>only</b> be called by the {@link AnimationsManager}.
     */
    public final void tick() {
        if(ticks == 0) {
            onStart();
        }
        ticks++;
        onTick();
        if(ticks >= getDuration()) {
            onFinish();
        }
    }

    /**
     * Called at every game-tick.
     */
    protected abstract void onTick();

    /**
     * Called once the animation is over.
     */
    protected abstract void onFinish();

    /**
     * Test if this animation is over.
     * @return {@code true} if the animation is over and will not tick anymore.
     */
    public final boolean isOver() {
        return ticks >= getDuration();
    }
}
