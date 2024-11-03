package fr.jamailun.ultimatespellsystem.bukkit.providers;

import fr.jamailun.ultimatespellsystem.bukkit.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.bukkit.runner.SpellRuntime;
import fr.jamailun.ultimatespellsystem.bukkit.spells.SpellEntity;
import fr.jamailun.ultimatespellsystem.bukkit.utils.StringTransformation;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.apache.logging.log4j.util.TriConsumer;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Provider for summon-properties.
 */
public class SummonPropertiesProvider extends UssProvider<SummonPropertiesProvider.SummonProperty> {
    private static final SummonPropertiesProvider INSTANCE = new SummonPropertiesProvider();
    public static SummonPropertiesProvider instance() {
        return INSTANCE;
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

    protected SummonProperty createForBukkitEntity(@NotNull TriConsumer<Entity, Object, SpellRuntime> base) {
        return (spellEntity, value, run) -> spellEntity.getBukkitEntity().ifPresent(be -> base.accept(be, value, run));
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
        register(createForEntity((age,baby,r) -> {if(baby) age.setBaby(); else age.setAdult();}, Ageable.class, Boolean.class), "baby", "is_baby");

        // Custom name
        register(createForEntity((entity,name,r) -> entity.customName(LegacyComponentSerializer.legacyAmpersand().deserialize(StringTransformation.transformString(name, r))), Entity.class, String.class), "name", "custom_name");
        register(createForEntity((entity,visible,r) -> entity.setCustomNameVisible(visible), Entity.class, Boolean.class), "name_visible", "custom_name_visible");
    }

    public interface SummonProperty extends TriConsumer<SpellEntity, Object, SpellRuntime> {}

}
