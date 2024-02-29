package fr.jamailun.ultimatespellsystem.bukkit.extensible;

import fr.jamailun.ultimatespellsystem.bukkit.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.bukkit.utils.KyoriAdaptor;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
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

    private SummonPropertiesExtension() {
        register((entity, value) -> {
            if(entity instanceof LivingEntity livingEntity) {
                if (value instanceof Double number) {
                    Objects.requireNonNull(livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(number);
                } else {
                    UltimateSpellSystem.logWarning("Invalid type for HEALTH: " + value);
                }
            }
        }, "health", "max_health");

        register((entity, value) -> {
            if(entity instanceof LivingEntity livingEntity) {
                if (value instanceof Double number) {
                    Objects.requireNonNull(livingEntity.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)).setBaseValue(number);
                } else {
                    UltimateSpellSystem.logWarning("Invalid type for HEALTH: " + value);
                }
            }
        }, "attack_damage", "attack", "damage");

        register((entity, value) -> {
            if(value instanceof String string) {
                entity.customName(KyoriAdaptor.adventure(string));
            } else {
                UltimateSpellSystem.logWarning("Invalid type for NAME: " + value);
            }
        }, "name", "custom_name");
    }

    public void register(SummonProperty applier, String key, String... otherKeys) {
        properties.put(key, applier);
        if(otherKeys != null) {
            for(String otherKey : otherKeys) {
                properties.put(otherKey, applier);
            }
        }
    }

    public @Nullable SummonProperty getApplier(String property) {
        return properties.get(property);
    }

    public interface SummonProperty extends BiConsumer<Entity, Object> {}

}
