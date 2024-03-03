package fr.jamailun.ultimatespellsystem.bukkit.extensible;

import fr.jamailun.ultimatespellsystem.bukkit.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.bukkit.spells.SpellEntity;
import fr.jamailun.ultimatespellsystem.bukkit.utils.KyoriAdaptor;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import java.util.Objects;
import java.util.function.BiConsumer;

/**
 * Provider for summon-properties.
 */
public class SummonPropertiesExtension extends UssProvider<SummonPropertiesExtension.SummonProperty> {
    private static final SummonPropertiesExtension INSTANCE = new SummonPropertiesExtension();
    public static SummonPropertiesExtension instance() {
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
        return (spellEntity, value) -> {
            spellEntity.getBukkitEntity().ifPresent(be -> {
                if(be instanceof LivingEntity living)
                    base.accept(living, value);
            });
        };
    }

    protected SummonProperty createForBukkitEntity(BiConsumer<Entity, Object> base) {
        return (spellEntity, value) -> spellEntity.getBukkitEntity().ifPresent(be -> base.accept(be, value));
    }

    private SummonPropertiesExtension() {
        register(createAttributeSetter(Attribute.GENERIC_MAX_HEALTH), "health", "max_health");
        register(createAttributeSetter(Attribute.GENERIC_ATTACK_DAMAGE), "attack_damage", "attack", "damage");
        register(createAttributeSetter(Attribute.GENERIC_ARMOR), "armor");
        register(createAttributeSetter(Attribute.GENERIC_ARMOR_TOUGHNESS), "toughness", "armor_toughness");
        register(createAttributeSetter(Attribute.GENERIC_MOVEMENT_SPEED), "speed", "movement_speed");

        register(createForBukkitEntity((entity, value) -> {
            if(value instanceof String string) {
                entity.customName(KyoriAdaptor.adventure(string));
            } else {
                UltimateSpellSystem.logWarning("Invalid type for NAME: " + value);
            }
        }), "name", "custom_name");
    }

    public interface SummonProperty extends BiConsumer<SpellEntity, Object> {}

}
