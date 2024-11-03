package fr.jamailun.ultimatespellsystem.bukkit.providers;

import fr.jamailun.ultimatespellsystem.bukkit.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.bukkit.spells.SpellEntity;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import java.util.Objects;
import java.util.function.BiConsumer;

/**
 * Provider for summon-properties.
 */
public class SummonPropertiesProvider extends UssProvider<SummonPropertiesProvider.SummonProperty> {
    private static final SummonPropertiesProvider INSTANCE = new SummonPropertiesProvider();
    public static SummonPropertiesProvider instance() {
        return INSTANCE;
    }

    // -- instance

    protected SummonProperty createAttributeSetter(Attribute attribute) {
        return createForLivingEntity((entity, value) -> {
            if (value instanceof Double number) {
                Objects.requireNonNull(entity.getAttribute(attribute)).setBaseValue(number);
            } else {
                UltimateSpellSystem.logWarning("Invalid type for "+attribute+": " + value);
            }
        });
    }

    protected SummonProperty createForLivingEntity(BiConsumer<LivingEntity, Object> base) {
        return createForEntity(base, LivingEntity.class);
    }

    protected <T extends Entity> SummonProperty createForEntity(BiConsumer<T, Object> base, Class<T> clazz) {
        return createForEntity(base, clazz, Object.class);
    }

    protected <T extends Entity, V> SummonProperty createForEntity(BiConsumer<T, V> base, Class<T> clazz, Class<V> classValue) {
        return (spellEntity, value) -> {
            spellEntity.getBukkitEntity().ifPresent(be -> {
                if(clazz.isInstance(be)) {
                    if(classValue.isInstance(value)) {
                        base.accept(clazz.cast(be), classValue.cast(value));
                    } else {
                        UltimateSpellSystem.logWarning("Invalid type for '"+value+"', expected " + classValue + "(" + value.getClass() + ")");
                    }
                }
            });
        };
    }

    protected SummonProperty createForBukkitEntity(BiConsumer<Entity, Object> base) {
        return (spellEntity, value) -> spellEntity.getBukkitEntity().ifPresent(be -> base.accept(be, value));
    }

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
        register(createForEntity((age,baby) -> {if(baby) age.setBaby(); else age.setAdult();}, Ageable.class, Boolean.class), "baby", "is_baby");

        // Custom name
        register(createForEntity((entity,name) -> entity.customName(LegacyComponentSerializer.legacyAmpersand().deserialize(name)), Entity.class, String.class), "name", "custom_name");
        register(createForEntity(Entity::setCustomNameVisible, Entity.class, Boolean.class), "name_visible", "custom_name_visible");
    }

    public interface SummonProperty extends BiConsumer<SpellEntity, Object> {}

}
