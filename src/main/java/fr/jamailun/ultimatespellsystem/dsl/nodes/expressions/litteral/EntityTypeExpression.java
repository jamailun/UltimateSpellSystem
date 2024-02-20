package fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.litteral;

import fr.jamailun.ultimatespellsystem.dsl.errors.SpellException;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Type;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenPosition;
import org.bukkit.entity.EntityType;

import java.util.List;
import java.util.NoSuchElementException;

public class EntityTypeExpression extends LiteralExpression {

    private final EntityType type;

    public EntityTypeExpression(TokenPosition position, EntityType type) {
        super(position);
        this.type = type;
    }

    public EntityType getRawType() {
        return type;
    }

    @Override
    public Type getExpressionType() {
        return TypePrimitive.ENTITY_TYPE.asType();
    }

    @Override
    public String toString() {
        return "EntityType." + type;
    }

    private final static List<EntityType> ALLOWED_ENTITY_TYPES = List.of(
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

    public static EntityType fromString(String value) {
        try {
            EntityType type = EntityType.valueOf(value);
            if(ALLOWED_ENTITY_TYPES.contains(type))
                return type;
            throw new SpellException("Unauthorized EntityType: " + value);
        } catch (Exception ignored) {
            return null;
        }
    }

}
