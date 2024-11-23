package fr.jamailun.ultimatespellsystem.plugin.animations;

import fr.jamailun.ultimatespellsystem.api.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.api.animations.Animation;
import fr.jamailun.ultimatespellsystem.api.animations.AnimationsManager;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

/**
 * Manages and run {@link Animation}.
 */
public final class AnimationsManagerImpl implements AnimationsManager {

    private final Map<Animation, Runnable> animations = new HashMap<>();
    private BukkitRunnable task;

    public void start() {
        if(task != null)
            throw new IllegalStateException("Cannot start an AnimationManager twice.");
        task = UltimateSpellSystem.runTaskRepeat(this::tick, 1, 1);
    }

    public void stop() {
        if(task == null)
            throw new IllegalStateException("Cannot stop an non-started AnimationManager.");
        task.cancel();
        task = null;
    }

    @Override
    public void play(@NotNull Animation animation, @Nullable Runnable callback) {
        animations.put(animation, Objects.requireNonNullElse(callback, () -> {}));
    }

    private void tick() {
        Iterator<Map.Entry<Animation, Runnable>> iterator = animations.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Animation, Runnable> entry = iterator.next();
            Animation animation = entry.getKey();

            if (animation.isOver()) {
                iterator.remove();
                entry.getValue().run();
                continue;
            }

            animation.tick();
        }
    }

    @Override
    public void purge() {
        animations.values().forEach(Runnable::run);
        animations.clear();
    }
}
