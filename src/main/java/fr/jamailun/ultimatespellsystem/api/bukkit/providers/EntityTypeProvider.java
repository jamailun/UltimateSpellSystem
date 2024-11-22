package fr.jamailun.ultimatespellsystem.api.bukkit.providers;

import fr.jamailun.ultimatespellsystem.api.bukkit.entities.UssEntityType;
import fr.jamailun.ultimatespellsystem.bukkit.entities.implem.Orb;
import fr.jamailun.ultimatespellsystem.dsl.registries.EntityTypeRegistry;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Provider for {@link UssEntityType}.
 */
public final class EntityTypeProvider extends UssProvider<UssEntityType> {
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

    private static boolean initialized = false;
    @ApiStatus.Internal
    public static synchronized void initialize() {
        if(initialized) return;
        initialized = true;

        // Bukkit
        for(EntityType type : EntityTypeRegistry.DEFAULT_TYPES) {
            String name = EntityTypeRegistry.prepare(type.name());
            instance().register(new UssEntityType(type), name);
        }
        // Custom
        instance().register(new UssEntityType(Orb.class), "orb");
    }
}
