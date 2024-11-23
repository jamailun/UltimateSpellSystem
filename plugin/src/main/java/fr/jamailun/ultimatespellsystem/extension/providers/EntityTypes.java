package fr.jamailun.ultimatespellsystem.extension.providers;

import fr.jamailun.ultimatespellsystem.api.entities.UssEntityType;
import fr.jamailun.ultimatespellsystem.api.providers.EntityTypeProvider;
import fr.jamailun.ultimatespellsystem.dsl.registries.EntityTypeRegistry;
import fr.jamailun.ultimatespellsystem.plugin.entities.implem.Orb;
import org.bukkit.entity.EntityType;

public final class EntityTypes {
    private EntityTypes() {}


    public static void register() {
        // Bukkit
        for(EntityType type : EntityTypeRegistry.DEFAULT_TYPES) {
            String name = EntityTypeRegistry.prepare(type.name());
            EntityTypeProvider.instance().register(new UssEntityType(type), name);
        }
        // Custom
        EntityTypeProvider.instance().register(new UssEntityType(Orb.class), "orb");
    }

}
