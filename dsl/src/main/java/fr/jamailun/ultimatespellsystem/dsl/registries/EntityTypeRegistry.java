package fr.jamailun.ultimatespellsystem.dsl.registries;

import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * You may need to allow specific tokens as "EntityType".
 * @see #allow(String)
 */
public final class EntityTypeRegistry {
    private EntityTypeRegistry() {}

    private static final Set<String> ALLOWED = new HashSet<>();

    /**
     * Allow a string to be allowed.
     * @param entityType an Entity-Type string.
     */
    public static void allow(@NotNull String entityType) {
        ALLOWED.add(prepare(entityType));
    }

    /**
     * Test if a string as been allowed.
     * @param entityType the entity-type to test.
     * @return true if the entity-type exists.
     */
    public static boolean isAllowed(@NotNull String entityType) {
        return ALLOWED.contains(prepare(entityType));
    }

    public static void disallow(@NotNull String entityType) {
        ALLOWED.remove(prepare(entityType));
    }

    public static String prepare(String string) {
        return string.toLowerCase().replace(' ', '_');
    }

    private static final Set<EntityType> FORBIDDEN = Set.of(
        EntityType.PLAYER, EntityType.AREA_EFFECT_CLOUD, EntityType.MARKER, EntityType.LEASH_KNOT, EntityType.PAINTING,
        EntityType.ITEM, EntityType.ITEM_FRAME, EntityType.ITEM_DISPLAY, EntityType.GLOW_ITEM_FRAME, EntityType.OMINOUS_ITEM_SPAWNER,
        EntityType.FISHING_BOBBER, EntityType.TEXT_DISPLAY, EntityType.INTERACTION, EntityType.BLOCK_DISPLAY, EntityType.FIREWORK_ROCKET
    );

    static {
        Arrays.stream(EntityType.values())
            .filter(e -> ! FORBIDDEN.contains(e))
            .forEach(e -> allow(e.name()));
    }

}
