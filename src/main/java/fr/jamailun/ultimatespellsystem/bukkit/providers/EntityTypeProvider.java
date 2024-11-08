package fr.jamailun.ultimatespellsystem.bukkit.providers;

import fr.jamailun.ultimatespellsystem.bukkit.entities.implem.Orb;
import fr.jamailun.ultimatespellsystem.bukkit.entities.UssEntityType;
import fr.jamailun.ultimatespellsystem.dsl.registries.EntityTypeRegistry;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;

public final class EntityTypeProvider extends UssProvider<UssEntityType> {
    private static final EntityTypeProvider INSTANCE = new EntityTypeProvider();
    public static EntityTypeProvider instance() {
        return INSTANCE;
    }

    static {
        // Bukkit
        for(EntityType type : EntityTypeRegistry.DEFAULT_TYPES) {
            String name = EntityTypeRegistry.prepare(type.name());
            instance().register(new UssEntityType(type), name);
        }
        // Custom
        instance().register(new UssEntityType(Orb.class), "orb");
    }

    @Override
    protected void postRegister(@NotNull String key, @NotNull UssEntityType type) {
        EntityTypeRegistry.allow(key);
    }

    public static void loadDefaults() {/* load class and run static blocks ! */}
}
