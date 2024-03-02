package fr.jamailun.ultimatespellsystem.dsl.registries;

import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * You may need to allow specific tokens as "EntityType".
 * @see #allow(String)
 */
public final class EntityTypeRegistry {
    private EntityTypeRegistry() {}

    private static final Set<String> ALLOWED = new HashSet<>();

    /**
     * Allow a string to be
     * @param entityType an Entity-Type string.
     */
    public static void allow(@NotNull String entityType) {
        ALLOWED.add(prepare(entityType));
    }

    public static boolean isAllowed(@NotNull String entityType) {
        return ALLOWED.contains(prepare(entityType));
    }

    public static void disallow(@NotNull String entityType) {
        ALLOWED.remove(prepare(entityType));
    }

    public static String prepare(String string) {
        return string.toLowerCase().replace(' ', '_');
    }

    public static final List<EntityType> DEFAULT_TYPES = List.of(
            EntityType.ELDER_GUARDIAN,
            EntityType.GUARDIAN,
            EntityType.ZOMBIE, EntityType.GIANT, EntityType.DROWNED,
            EntityType.ZOMBIE_VILLAGER, EntityType.ZOMBIFIED_PIGLIN, EntityType.ZOMBIE_HORSE,
            EntityType.HUSK, EntityType.SKELETON,
            EntityType.SKELETON_HORSE, EntityType.WITHER_SKELETON,
            EntityType.VILLAGER, EntityType.PILLAGER, EntityType.EVOKER, EntityType.ILLUSIONER, EntityType.RAVAGER,
            EntityType.VINDICATOR,
            EntityType.VEX, EntityType.STRAY, EntityType.HUSK,
            EntityType.SHULKER, EntityType.ALLAY,
            EntityType.PRIMED_TNT, EntityType.ARMOR_STAND,
            EntityType.CREEPER, EntityType.WITCH, EntityType.SILVERFISH,
            EntityType.SLIME, EntityType.GHAST, EntityType.BLAZE, EntityType.MAGMA_CUBE,
            EntityType.HOGLIN, EntityType.PIGLIN, EntityType.PIGLIN_BRUTE,
            EntityType.STRIDER, EntityType.ZOGLIN,

            EntityType.COW, EntityType.SALMON, EntityType.PUFFERFISH, EntityType.COD, EntityType.TROPICAL_FISH,
            EntityType.PIG, EntityType.CAT, EntityType.DOLPHIN, EntityType.WOLF,
            EntityType.TURTLE, EntityType.PANDA, EntityType.POLAR_BEAR, EntityType.BAT,
            EntityType.SHEEP, EntityType.CHICKEN, EntityType.SQUID, EntityType.GLOW_SQUID,
            EntityType.DONKEY, EntityType.MULE, EntityType.HORSE, EntityType.LLAMA,
            EntityType.MUSHROOM_COW, EntityType.OCELOT, EntityType.PARROT, EntityType.TRADER_LLAMA,
            EntityType.WANDERING_TRADER, EntityType.BEE, EntityType.FOX, EntityType.GOAT, EntityType.AXOLOTL,
            EntityType.FROG, EntityType.TADPOLE, EntityType.CAMEL,

            EntityType.IRON_GOLEM, EntityType.SNOWMAN,
            EntityType.SPIDER, EntityType.CAVE_SPIDER,

            EntityType.WITHER, EntityType.SNIFFER,
            EntityType.ENDERMAN, EntityType.ENDERMITE, EntityType.ENDER_CRYSTAL, EntityType.ENDER_DRAGON,
            EntityType.PHANTOM, EntityType.WARDEN,
            EntityType.LIGHTNING
    );

    static {
        DEFAULT_TYPES.forEach(et -> allow(et.name()));
    }

}
