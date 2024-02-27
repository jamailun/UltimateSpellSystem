package fr.jamailun.ultimatespellsystem.extensible;

import fr.jamailun.ultimatespellsystem.UltimateSpellSystem;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;

public class SummonPropertiesExtension {
    private static final SummonPropertiesExtension INSTANCE = new SummonPropertiesExtension();
    public static SummonPropertiesExtension instance() {
        return INSTANCE;
    }

    // -- instance

    private final Map<String, SummonProperty> properties = new HashMap<>();

    @SuppressWarnings( "deprecation" )
    private SummonPropertiesExtension() {
        register("health", (entity, value) -> {
            if(value instanceof Double number) {
                Objects.requireNonNull(entity.getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(number);
            } else {
                UltimateSpellSystem.logWarning("Invalid type for HEALTH: " + value);
            }
        });

        register("attack_damage", (entity, value) -> {
            if(value instanceof Double number) {
                Objects.requireNonNull(entity.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)).setBaseValue(number);
            } else {
                UltimateSpellSystem.logWarning("Invalid type for HEALTH: " + value);
            }
        });

        register("name", (entity, value) -> {
            if(value instanceof String string) {
                // Deprecated, but the new "component" system is a real pain in the ass to use.
                entity.setCustomName(string);
            } else {
                UltimateSpellSystem.logWarning("Invalid type for NAME: " + value);
            }
        });
    }

    public void register(String key, SummonProperty applier) {
        properties.put(key, applier);
    }

    public @Nullable SummonProperty getApplier(String property) {
        return properties.get(property);
    }

    public interface SummonProperty extends BiConsumer<LivingEntity, Object> {}

}
