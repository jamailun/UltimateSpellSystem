package fr.jamailun.ultimatespellsystem.api.providers;

import fr.jamailun.ultimatespellsystem.api.utils.ParticleShaper;
import org.jetbrains.annotations.NotNull;

/**
 * A provider for {@link ParticleShaper}, used by particle emissions and animations.
 */
public final class ParticleShapeProvider extends UssProvider<ParticleShaper> {
    private ParticleShapeProvider() {}
    private static final ParticleShapeProvider INSTANCE = new ParticleShapeProvider();

    /**
     * Get the non-null instance.
     * @return the instance.
     */
    public static @NotNull ParticleShapeProvider instance() {
        return INSTANCE;
    }

}
