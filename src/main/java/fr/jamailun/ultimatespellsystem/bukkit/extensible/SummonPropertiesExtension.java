package fr.jamailun.ultimatespellsystem.bukkit.extensible;

import fr.jamailun.ultimatespellsystem.bukkit.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.bukkit.spells.SpellEntity;
import fr.jamailun.ultimatespellsystem.bukkit.utils.KyoriAdaptor;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;

/**
 *
 */
public final class SummonPropertiesExtension {
    private static final SummonPropertiesExtension INSTANCE = new SummonPropertiesExtension();
    public static SummonPropertiesExtension instance() {
        return INSTANCE;
    }

    // -- instance

    private final Map<String, SummonProperty> properties = new HashMap<>();

    private SummonProperty createAttributeSetter(Attribute attribute) {
        return createForLivingEntity((entity, value) -> {
            if (value instanceof Double number) {
                Objects.requireNonNull(entity.getAttribute(attribute)).setBaseValue(number);
            } else {
                UltimateSpellSystem.logWarning("Invalid type for "+attribute+": " + value);
            }
        });
    }

    private SummonProperty createForLivingEntity(BiConsumer<LivingEntity, Object> base) {
        return (spellEntity, value) -> {
            spellEntity.getBukkitEntity().ifPresent(be -> {
                if(be instanceof LivingEntity living)
                    base.accept(living, value);
            });
        };
    }
    private SummonProperty createForBukkitEntity(BiConsumer<Entity, Object> base) {
        return (spellEntity, value) -> spellEntity.getBukkitEntity().ifPresent(be -> base.accept(be, value));
    }

    private SummonPropertiesExtension() {
        registerApplierEntity(createAttributeSetter(Attribute.GENERIC_MAX_HEALTH), "health", "max_health");
        registerApplierEntity(createAttributeSetter(Attribute.GENERIC_ATTACK_DAMAGE), "attack_damage", "attack", "damage");
        registerApplierEntity(createAttributeSetter(Attribute.GENERIC_ARMOR), "armor");
        registerApplierEntity(createAttributeSetter(Attribute.GENERIC_ARMOR_TOUGHNESS), "toughness", "armor_toughness");
        registerApplierEntity(createAttributeSetter(Attribute.GENERIC_MOVEMENT_SPEED), "speed", "movement_speed");

        registerApplierEntity(createForBukkitEntity((entity, value) -> {
            if(value instanceof String string) {
                entity.customName(KyoriAdaptor.adventure(string));
            } else {
                UltimateSpellSystem.logWarning("Invalid type for NAME: " + value);
            }
        }), "name", "custom_name");
    }

    public void registerApplierEntity(SummonProperty applier, String key, String... otherKeys) {
        properties.put(key, applier);
        if(otherKeys != null) {
            for(String otherKey : otherKeys) {
                properties.put(otherKey, applier);
            }
        }
    }

    public Optional<SummonProperty> getApplier(String property) {
        return Optional.ofNullable(properties.get(property));
    }

    public interface SummonProperty extends BiConsumer<SpellEntity, Object> {}

}
