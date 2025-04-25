package fr.jamailun.ultimatespellsystem.extension.providers;

import fr.jamailun.ultimatespellsystem.api.entities.EntityScope;
import fr.jamailun.ultimatespellsystem.api.providers.ScopeProvider;
import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Register custom scopes.
 */
public final class Scopes {
    private Scopes() {}

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
    private static final List<EntityType> PILLAGER_TYPES = List.of(
            EntityType.ILLUSIONER, EntityType.PILLAGER, EntityType.VINDICATOR, EntityType.EVOKER,
            EntityType.RAVAGER, EntityType.VEX, EntityType.WITCH
    );

    private static @NotNull EntityScope of(@NotNull String tag, @NotNull List<EntityType> list) {
        return e -> list.contains(e.getType()) || e.getScoreboardTags().contains(tag);
    }

    private static boolean isPlayer(@NotNull Entity e) {
        if(e instanceof Player p) {
            return p.getGameMode() != GameMode.SPECTATOR && p.getGameMode() != GameMode.CREATIVE;
        }
        if(e.getScoreboardTags().contains("player")) {
            return true;
        }
        return !e.getScoreboardTags().contains("CITIZENS_NPC");
    }

    public static void register() {
        ScopeProvider.instance().register(of("undead", UNDEAD_TYPES), "undead", "undead_monster", "undeads_monster");
        ScopeProvider.instance().register(of("end", ENDER_TYPES), "end", "end_monster");
        ScopeProvider.instance().register(of("spider", SPIDER_TYPES), "spider", "spider_monster", "arthropod", "arthropod_monsters", "arthropods_monsters");
        ScopeProvider.instance().register(of("illager", PILLAGER_TYPES), "illager", "pillagers");
        ScopeProvider.instance().register(e -> e.getScoreboardTags().contains("CITIZENS_NPC"), "citizen");

        // Override player
        ScopeProvider.instance().register(Scopes::isPlayer, "player");
    }

}
