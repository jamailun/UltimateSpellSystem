package fr.jamailun.ultimatespellsystem.extension.animations;

import fr.jamailun.ultimatespellsystem.api.providers.AnimationsProvider;

/**
 * Internal registration for animations.
 */
public final class AnimationExtension {
  private AnimationExtension() {}

  /**
   * Register animations.
   */
  public static void register() {
    AnimationsProvider.instance().register(AnimationItemsExplode.generator(), AnimationItemsExplode.ID);
    AnimationsProvider.instance().register(AnimationParticleCircle.generator(), AnimationParticleCircle.ID);
    AnimationsProvider.instance().register(AnimationParticleSpiraling.generator(), AnimationParticleSpiraling.ID);
    AnimationsProvider.instance().register(AnimationBlockCircle.generator(), AnimationBlockCircle.ID);
    AnimationsProvider.instance().register(AnimationBlockSquare.generator(), AnimationBlockSquare.ID);
  }

}
