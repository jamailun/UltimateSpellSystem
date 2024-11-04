package fr.jamailun.ultimatespellsystem.bukkit.providers;

import fr.jamailun.ultimatespellsystem.bukkit.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.bukkit.runner.SpellRuntime;
import fr.jamailun.ultimatespellsystem.bukkit.spells.SpellEntity;
import fr.jamailun.ultimatespellsystem.bukkit.utils.StringTransformation;
import org.apache.logging.log4j.util.TriConsumer;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Provider for summon-properties.
 */
public class SummonPropertiesProvider extends UssProvider<SummonPropertiesProvider.SummonProperty> {
    private static final SummonPropertiesProvider INSTANCE = new SummonPropertiesProvider();
    public static SummonPropertiesProvider instance() {
        return INSTANCE;
    }

    /**
     * Consumer interface.
     */
    public interface SummonProperty extends TriConsumer<SpellEntity, Object, SpellRuntime> {}

    private SummonPropertiesProvider() {
        // Statistics attributes
        register(createAttributeSetter(Attribute.GENERIC_MAX_HEALTH), "health", "max_health");
        register(createAttributeSetter(Attribute.GENERIC_ATTACK_DAMAGE), "attack_damage", "attack", "damage");
        register(createAttributeSetter(Attribute.GENERIC_ARMOR), "armor");
        register(createAttributeSetter(Attribute.GENERIC_ARMOR_TOUGHNESS), "toughness", "armor_toughness");
        register(createAttributeSetter(Attribute.GENERIC_MOVEMENT_SPEED), "speed", "movement_speed");
        register(createAttributeSetter(Attribute.GENERIC_KNOCKBACK_RESISTANCE), "kbr", "knockback_resistance", "kb_resistance");
        register(createAttributeSetter(Attribute.GENERIC_ATTACK_KNOCKBACK), "kb", "knockback", "attack_knockback");

        // Baby tag
        register(createForEntity((age,baby,r) -> {if(baby) age.setBaby(); else age.setAdult();}, Ageable.class, Boolean.class), "baby", "is_baby");

        // Custom name
        register(createForEntity((entity,name,r) -> entity.customName(StringTransformation.parse(StringTransformation.transformString(name, r))), Entity.class, String.class), "name", "custom_name");
        register(createForEntity((entity,visible,r) -> entity.setCustomNameVisible(visible), Entity.class, Boolean.class), "name_visible", "custom_name_visible");

        // Equipment
        register(createEquipment(EquipmentSlot.HEAD), "head", "helmet");
        register(createEquipment(EquipmentSlot.CHEST), "chest", "chestplate");
        register(createEquipment(EquipmentSlot.LEGS), "legs", "leggings");
        register(createEquipment(EquipmentSlot.FEET), "feet", "boots");
        register(createEquipment(EquipmentSlot.HAND), "hand", "main_hand", "right_hand");
        register(createEquipment(EquipmentSlot.OFF_HAND), "off_hand", "left_hand", "offhand");
    }

    // -- instance

    protected @NotNull SummonProperty createAttributeSetter(@NotNull Attribute attribute) {
        return createForLivingEntity((entity, value, run) -> {
            if (value instanceof Double number) {
                Objects.requireNonNull(entity.getAttribute(attribute)).setBaseValue(number);
            } else {
                UltimateSpellSystem.logWarning("Invalid type for "+attribute+": " + value);
            }
        });
    }

    protected @NotNull SummonProperty createForLivingEntity(@NotNull TriConsumer<LivingEntity, Object, SpellRuntime> base) {
        return createForEntity(base, LivingEntity.class);
    }

    protected <T extends Entity> @NotNull SummonProperty createForEntity(@NotNull TriConsumer<T, Object, SpellRuntime> base, @NotNull Class<T> clazz) {
        return createForEntity(base, clazz, Object.class);
    }

    protected <T extends Entity, V> @NotNull SummonProperty createForEntity(@NotNull TriConsumer<T, V, SpellRuntime> base, @NotNull Class<T> clazz, @NotNull Class<V> classValue) {
        return (spellEntity, value, run) -> {
            spellEntity.getBukkitEntity().ifPresent(be -> {
                if(clazz.isInstance(be)) {
                    if(classValue.isInstance(value)) {
                        base.accept(clazz.cast(be), classValue.cast(value), run);
                    } else {
                        UltimateSpellSystem.logWarning("Invalid type for '"+value+"', expected " + classValue + "(" + value.getClass() + ")");
                    }
                }
            });
        };
    }

    protected @NotNull SummonProperty createForBukkitEntity(@NotNull TriConsumer<Entity, Object, SpellRuntime> base) {
        return (spellEntity, value, run) -> spellEntity.getBukkitEntity().ifPresent(be -> base.accept(be, value, run));
    }

    protected @NotNull SummonProperty createEquipment(@NotNull EquipmentSlot slot) {
        return createForEntity((entity,mapRaw,run) -> {
            Map<String, Object> map = convertMap(mapRaw);

            // Type of the equipment
            String type = readMap(map, "type", String.class, Material.AIR.name());
            Material material;
            try {
                material = Material.valueOf(type.toUpperCase());
            } catch(IllegalArgumentException e) {
                UltimateSpellSystem.logWarning("Illegal item for slot "+slot+" : " + type + ".");
                material = Material.AIR;
            }

            // Amount
            int amount = readMap(map, "amount", Integer.class, 1);
            int damage = readMap(map, "damage", Integer.class, 0);
            String name = readMap(map, "type", String.class, null);
            List<?> lore = readMap(map, "lore", List.class, null);

            UltimateSpellSystem.logInfo("DROPPABLE type = " + (map.containsKey("droppable") ? map.get("droppable") + "|" + map.get("droppable").getClass() : "null"));
            UltimateSpellSystem.logInfo("LORE type = " + (map.containsKey("lore") ? map.get("lore") + "|" + map.get("lore").getClass() : "null"));

            boolean droppable = readMap(map, "droppable", Boolean.class, false);

            ItemStack item = new ItemStack(material, amount);
            ItemMeta meta = item.getItemMeta();
            if(meta != null) {
                if(name != null)
                    meta.displayName(StringTransformation.parse(StringTransformation.transformString(name, run)));
                if(lore != null)
                    meta.lore(
                        lore.stream()
                            .filter(Objects::nonNull)
                            .map(Objects::toString)
                            .map(line -> StringTransformation.parse("&7&o" + StringTransformation.transformString(line, run)))
                            .toList()
                    );
            }
            if(meta instanceof Damageable damageMeta) {
                damageMeta.setDamage(damage);
            }
            item.setItemMeta(meta);

            if(entity.getEquipment() != null)
                entity.getEquipment().setItem(slot, item);
        }, LivingEntity.class, Map.class);
    }

    @SuppressWarnings("unchecked")
    protected @NotNull Map<String, Object> convertMap(@NotNull Map<?,?> map) {
        try {
            return (Map<String, Object>) map;
        } catch(ClassCastException e) {
            UltimateSpellSystem.logError("Could not convert map " + map + ".");
            return new HashMap<>();
        }
    }

    protected <T> T readMap(@NotNull Map<String,Object> map, @NotNull String key, @NotNull Class<T> clazz, @Nullable T defaultValue) {
        Object object = map.get(key);
        if(object == null) return defaultValue;
        try {
            return clazz.cast(object);
        } catch(ClassCastException e) {
            return defaultValue;
        }
    }

}
