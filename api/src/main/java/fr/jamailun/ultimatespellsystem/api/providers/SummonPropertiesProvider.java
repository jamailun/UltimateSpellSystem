package fr.jamailun.ultimatespellsystem.api.providers;

import fr.jamailun.ultimatespellsystem.api.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.api.entities.SummonAttributes;
import fr.jamailun.ultimatespellsystem.api.runner.SpellRuntime;
import fr.jamailun.ultimatespellsystem.api.entities.SpellEntity;
import fr.jamailun.ultimatespellsystem.api.utils.StringTransformation;
import org.apache.logging.log4j.util.TriConsumer;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

/**
 * Provider for summon-properties.
 */
public class SummonPropertiesProvider extends UssProvider<SummonPropertiesProvider.SummonProperty> {
    private static final SummonPropertiesProvider INSTANCE;

    /**
     * Get the instance.
     * @return the non-null instance.
     */
    public static @NotNull SummonPropertiesProvider instance() {
        return INSTANCE;
    }

    /**
     * Boolean. If true, the mob can drop equipment. Default: false.
     */
    public static final String ATTRIBUTE_MOB_CAN_DROP = "drop";
    /**
     * Boolean. If true, the mob can attack its own master. Default: false.
     */
    public static final String ATTRIBUTE_MOB_CAN_AGGRO_MASTER = "can_aggro_caster";
    /**
     * Boolean. If true, the mob can attack its master's other summons. Default: false.
     */
    public static final String ATTRIBUTE_MOB_CAN_AGGRO_SUMMONS = "can_aggro_summons";
    /**
     * Boolean. If true, the mob can attack its master's other summons. Default: false.
     */
    public static final String ATTRIBUTE_MOB_CAN_AGGRO_ALLIES = "can_aggro_allies";
    /**
     * Scope. The kind of entities the summon can aggro. Default = none.
     */
    public static final String ATTRIBUTE_MOB_AGGRO_SCOPE = "aggro_scope";
    /**
     * Double. The range at which the summon will aggro an entity. Default = 7.
     */
    public static final String ATTRIBUTE_MOB_AGGRO_RANGE = "aggro_range";

    /**
     * Boolean. If true, the projectile cannot deal damage to a caster.
     */
    public static final String ATTRIBUTE_PROJECTILE_CAN_DAMAGE_CASTER = "can_damage_caster";

    /**
     * Boolean. If true, the projectile cannot deal damage to an ally.
     */
    public static final String ATTRIBUTE_PROJECTILE_CAN_DAMAGE_ALLIES = "can_damage_allies";

    /**
     * The spell context.
     */
    public interface Context {
        /**
         * Get the runtime, used to evaluate expressions.
         * @return a non-null instance.
         */
        @NotNull SpellRuntime runtime();

        /**
         * Get the attributes of the summoned entity.
         * @return a non-null instance.
         */
        @NotNull SummonAttributes attributes();

        /**
         * Send a warning about an invalid type.
         * @param name name of the property, or context.
         * @param expected expected type.
         * @param actual obtained object.
         */
        void invalidTypeWarn(@NotNull String name, @NotNull Class<?> expected, @Nullable Object actual);
    }

    /**
     * Consumer interface.
     */
    public interface SummonProperty extends TriConsumer<SpellEntity, Object, Context> {}

    static {
        INSTANCE = new SummonPropertiesProvider();

        // Useful tags
        registerBlank(ATTRIBUTE_MOB_CAN_DROP);
        registerBlank(ATTRIBUTE_MOB_CAN_AGGRO_MASTER);
        registerBlank(ATTRIBUTE_MOB_CAN_AGGRO_SUMMONS);
        registerBlank(ATTRIBUTE_MOB_CAN_AGGRO_ALLIES);
        registerBlank(ATTRIBUTE_PROJECTILE_CAN_DAMAGE_ALLIES);
        registerBlank(ATTRIBUTE_PROJECTILE_CAN_DAMAGE_CASTER);
        registerBlank(ATTRIBUTE_MOB_CAN_DROP);
        registerBlank(ATTRIBUTE_MOB_AGGRO_SCOPE);
        registerBlank(ATTRIBUTE_MOB_AGGRO_RANGE);
    }

    private SummonPropertiesProvider() {
        // Statistics attributes
        register(createForLivingEntity((entity, value, ctx) -> {
            if (value instanceof Double number) {
                Objects.requireNonNull(entity.getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(number);
                entity.setHealth(number);
            } else {
                ctx.invalidTypeWarn("max_health", Double.class, value);
            }
        }), "health", "max_health");
        register(createAttributeSetter(Attribute.GENERIC_ATTACK_DAMAGE), "attack_damage", "attack", "damage");
        register(createAttributeSetter(Attribute.GENERIC_ARMOR), "armor");
        register(createAttributeSetter(Attribute.GENERIC_ARMOR_TOUGHNESS), "toughness", "armor_toughness");
        register(createAttributeSetter(Attribute.GENERIC_MOVEMENT_SPEED), "speed", "movement_speed");
        register(createAttributeSetter(Attribute.GENERIC_KNOCKBACK_RESISTANCE), "kbr", "knockback_resistance", "kb_resistance");
        register(createAttributeSetter(Attribute.GENERIC_ATTACK_KNOCKBACK), "kb", "knockback", "attack_knockback");

        // Baby tag
        register(createForEntity((age,baby,r) -> {if(baby) age.setBaby(); else age.setAdult();}, Ageable.class, Boolean.class), "baby", "is_baby");

        // Custom name
        register(createForEntity((entity,name,c) -> entity.customName(StringTransformation.parse(StringTransformation.transformString(name, c.runtime()))), Entity.class, String.class), "name", "custom_name");
        register(createForEntity((entity,visible,r) -> entity.setCustomNameVisible(visible), Entity.class, Boolean.class), "name_visible", "custom_name_visible");

        // Equipment
        register(createEquipment(EquipmentSlot.HEAD), "head", "helmet");
        register(createEquipment(EquipmentSlot.CHEST), "chest", "chestplate");
        register(createEquipment(EquipmentSlot.LEGS), "legs", "leggings");
        register(createEquipment(EquipmentSlot.FEET), "feet", "boots");
        register(createEquipment(EquipmentSlot.HAND), "hand", "main_hand", "right_hand");
        register(createEquipment(EquipmentSlot.OFF_HAND), "off_hand", "left_hand", "offhand");

        // Armor stand tags
        register(createForEntity((as,val,r) -> as.setSmall(val), ArmorStand.class, Boolean.class), "small");
        register(createForEntity((as,val,r) -> as.setMarker(val), ArmorStand.class, Boolean.class), "marker");
        register(createForEntity((as,val,r) -> as.setBasePlate(val), ArmorStand.class, Boolean.class), "base_plate");
        register(createForEntity((as,val,r) -> as.setVisible(val), ArmorStand.class, Boolean.class), "visible");
        register(createForEntity((as,val,r) -> as.setArms(val), ArmorStand.class, Boolean.class), "show_arms");
    }

    // -- instance

    /**
     * Util method to provide customization on an {@link Attribute}.
     * @param attribute the attribute to set.
     * @return a summon property, accepting a {@link LivingEntity} as target and a {@link Double} as a value.
     */
    public static @NotNull SummonProperty createAttributeSetter(@NotNull Attribute attribute) {
        return createForLivingEntity((entity, value, ctx) -> {
            if (value instanceof Double number) {
                Objects.requireNonNull(entity.getAttribute(attribute)).setBaseValue(number);
            } else {
                ctx.invalidTypeWarn(attribute.name().toLowerCase(), Double.class, value);
            }
        });
    }

    /**
     * Create a summon-property, using a filter on the entity class: it must be a {@link LivingEntity}.
     * @param base a base consumer. Accepts a living-entity and a value, alongside the context (containing the runtime).
     * @return a summon property than will trigger the base consumer only when the runtime execution provide valid types for the entity.
     */
    public static @NotNull SummonProperty createForLivingEntity(@NotNull TriConsumer<LivingEntity, Object, Context> base) {
        return createForEntity(base, LivingEntity.class);
    }

    /**
     * Create a summon-property, using a filter on the entity class.
     * @param base a base consumer. Takes the generic entity and value, alongside the context (containing the runtime).
     * @param clazz the class to provide for the entity. If the provided entity is <b>not</b> of this class, the {@code base} will not be executed.
     * @return a summon property than will trigger the base consumer only when the runtime execution provide valid types for the generics.
     * @param <T> the generic type of the entity. If the provided element is <b>not</b> of this class, the {@code base} will not be executed.
     */
    public static <T extends Entity> @NotNull SummonProperty createForEntity(@NotNull TriConsumer<T, Object, Context> base, @NotNull Class<T> clazz) {
        return createForEntity(base, clazz, Object.class);
    }

    /**
     * Create a summon-property, using a filter on the entity class <b>and</b> the value class.
     * @param base a base consumer. Takes the generic entity and value, alongside the runtime.
     * @param clazz the class to provide for the entity. If the provided entity is <b>not</b> of this class, the {@code base} will not be executed.
     * @param classValue the class to provide for the value. If the provided value is <b>not</b> of this class, the {@code base} will not be executed.
     * @return a summon property than will trigger the base consumer only when the runtime execution provide valid types for the generics.
     * @param <T> the generic type of the entity. If the provided element is <b>not</b> of this class, the {@code base} will not be executed.
     * @param <V> the generic type of the value. Can be anything, but cannot really differ from classic {@link String}, {@link Double} or {@link Number},
     *           or even {@link java.util.List} and {@link Map}. If the provided element is <b>not</b> of this class, the {@code base} will not be executed.
     */
    public static <T extends Entity, V> @NotNull SummonProperty createForEntity(@NotNull TriConsumer<T, V, Context> base, @NotNull Class<T> clazz, @NotNull Class<V> classValue) {
        return (spellEntity, value, ctx) -> spellEntity.getBukkitEntity().ifPresent(be -> {
            if(clazz.isInstance(be)) {
                if(classValue.isInstance(value)) {
                    base.accept(clazz.cast(be), classValue.cast(value), ctx);
                } else {
                    ctx.invalidTypeWarn("(? entity)", classValue, value);
                }
            }
        });
    }

    /**
     * Create an equipment setter.
     * @param slot the slot to set.
     * @return a new summon property.
     */
    public static @NotNull SummonProperty createEquipment(@NotNull EquipmentSlot slot) {
        return createForEntity((entity,mapRaw,ctx) -> {
            ItemStack item = UltimateSpellSystem.getItemReader().readFromMap(mapRaw, ctx.runtime(), "slot " + slot);
            if(entity.getEquipment() != null)
                entity.getEquipment().setItem(slot, item);
        }, LivingEntity.class, Map.class);
    }

    /**
     * Register a blank key as used. This allows to ignore some warnings.
     * @param keys non-null keys to ignore.
     */
    public static void registerBlank(@NotNull String @NotNull... keys) {
        Arrays.stream(keys)
            .filter(k -> !instance().exists(k))
            .forEach(k -> instance().register((spellEntity, o, context) -> {}, k));
    }

}
