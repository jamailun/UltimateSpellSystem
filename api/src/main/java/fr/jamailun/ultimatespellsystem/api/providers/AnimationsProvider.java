package fr.jamailun.ultimatespellsystem.api.providers;

import fr.jamailun.ultimatespellsystem.api.animations.Animation;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;

/**
 * A provider to register {@link Animation Animations}.
 */
public final class AnimationsProvider extends UssProvider<AnimationsProvider.AnimationGenerator> {
  private AnimationsProvider() {}
  private static final AnimationsProvider INSTANCE = new AnimationsProvider();
  /**
   * Get the instance.
   * @return the non-null, existing instance.
   */
  public static @NotNull AnimationsProvider instance() {
    return INSTANCE;
  }

  /**
   * Generate the {@link Animation} new instance.
   * @param id the non-null id of the animation to create.
   * @param location in-world location to play the animation at.
   * @param properties properties to use for this animation.
   * @return {@code null} if {@link #exists(String)} returns {@code false}, or if the properties could not generate a proper animation instance.
   */
  public @Nullable Animation generate(@NotNull String id, @NotNull Location location, @NotNull Map<String, Object> properties) {
    return Optional.ofNullable(find(id))
        .map(a -> a.generate(location, properties))
        .orElse(null);
  }

  /**
   * A generator for an {@link Animation}.
   */
  public interface AnimationGenerator {

    /**
     * Generate an instance {@link Animation}, with the provided arguments.
     * @param location in-world location to play the animation at.
     * @param data a properties set for this animation.
     * @return {@code null} if the properties are not valid for this animation. It's your job to log any error for the end-user.
     */
    @Nullable Animation generate(@NotNull Location location, @NotNull Map<String, Object> data);
  }

}
