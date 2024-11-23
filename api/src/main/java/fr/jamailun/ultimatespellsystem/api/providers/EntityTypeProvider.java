package fr.jamailun.ultimatespellsystem.api.providers;

import fr.jamailun.ultimatespellsystem.api.entities.UssEntityType;
import fr.jamailun.ultimatespellsystem.dsl.registries.EntityTypeRegistry;
import org.jetbrains.annotations.NotNull;

/**
 * Provider for {@link UssEntityType}.
 */
public final class EntityTypeProvider extends UssProvider<UssEntityType> {
    private EntityTypeProvider() {}
    private static final EntityTypeProvider INSTANCE = new EntityTypeProvider();

    /**
     * Get the instance.
     * @return the non-null, existing instance.
     */
    public static @NotNull EntityTypeProvider instance() {
        return INSTANCE;
    }

    @Override
    protected void postRegister(@NotNull String key, @NotNull UssEntityType type) {
        // Also register to the DSL.
        EntityTypeRegistry.allow(key);
    }
}
