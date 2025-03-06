package fr.jamailun.ultimatespellsystem.extension.providers;

import fr.jamailun.ultimatespellsystem.api.entities.UssEntityType;
import fr.jamailun.ultimatespellsystem.api.providers.EntityTypeProvider;
import fr.jamailun.ultimatespellsystem.dsl.registries.EntityTypeRegistry;
import fr.jamailun.ultimatespellsystem.plugin.entities.implem.Orb;
import org.bukkit.entity.EntityType;

/**
 * Will register Bukkit types from the DSL to {@link EntityTypeRegistry}.
 * <br/>
 * Will also register custom entity types.
 */
public final class EntityTypes {
    private EntityTypes() {}

    public static void register() {
        // Bukkit (only already allowed)
        for(EntityType type : EntityType.values()) {
            if(EntityTypeRegistry.isAllowed(type.name())) {
                EntityTypeProvider.instance().register(new UssEntityType(type), EntityTypeRegistry.prepare(type.name()));
            }
        }
        // Custom
        EntityTypeProvider.instance().register(new UssEntityType(Orb.class), "orb");
    }

}
