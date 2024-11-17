package fr.jamailun.ultimatespellsystem.extension.scopes;

import fr.jamailun.ultimatespellsystem.bukkit.providers.ScopeProvider;
import org.bukkit.entity.EntityType;

import java.util.List;

public final class EntityTypesScopes {
    private EntityTypesScopes() {}

    private static final List<EntityType> UNDEAD_TYPES = List.of(
            EntityType.SKELETON, EntityType.SKELETON_HORSE,
            EntityType.WITHER, EntityType.WITHER_SKELETON,
            EntityType.ZOMBIE, EntityType.ZOMBIE_VILLAGER, EntityType.ZOMBIE_HORSE, EntityType.DROWNED,
            EntityType.ZOMBIFIED_PIGLIN,  EntityType.ZOGLIN,
            EntityType.STRAY, EntityType.BOGGED, EntityType.HUSK,
            EntityType.PHANTOM
    );
    private static final List<EntityType> ENDER_TYPES = List.of(
            EntityType.ENDERMITE, EntityType.ENDER_DRAGON, EntityType.ENDERMAN,
            EntityType.SHULKER,
            EntityType.END_CRYSTAL, EntityType.SHULKER_BULLET, EntityType.ENDER_PEARL
    );
    private static final List<EntityType> SPIDER_TYPES = List.of(
            EntityType.SPIDER, EntityType.CAVE_SPIDER
    );

    public static void register() {
        ScopeProvider.instance().register(e -> UNDEAD_TYPES.contains(e.getType()), "undead", "undead_monster", "undeads_monster");
        ScopeProvider.instance().register(e -> ENDER_TYPES.contains(e.getType()), "end", "end_monster");
        ScopeProvider.instance().register(e -> SPIDER_TYPES.contains(e.getType()), "spider", "spider_monster", "arthropod", "arthropod_monsters", "arthropods_monsters");
    }

}
