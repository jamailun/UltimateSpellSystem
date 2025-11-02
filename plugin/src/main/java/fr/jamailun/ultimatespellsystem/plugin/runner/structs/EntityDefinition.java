package fr.jamailun.ultimatespellsystem.plugin.runner.structs;

import fr.jamailun.ultimatespellsystem.api.entities.SpellEntity;
import fr.jamailun.ultimatespellsystem.api.runner.structs.Struct;
import fr.jamailun.ultimatespellsystem.api.utils.StringTransformation;
import fr.jamailun.ultimatespellsystem.dsl2.library.StructDefinition;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.expressions.functions.FunctionArgument;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.dsl2.registries.ObjectsDefinitionRegistry;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Function;

/**
 * "Implements" the abstract {@link fr.jamailun.ultimatespellsystem.dsl2.library.structs.EntityStruct}.
 */
public class EntityDefinition extends AbstractStructDefinition<SpellEntity> {

    /**
     * Create a new definition.
     */
    public EntityDefinition() {
        super("entity");
        loadFields();
        loadFunctions();
    }

    @Override
    public @NotNull Struct instantiate(SpellEntity value) {
        return new EntityInstance(value, this);
    }

    private void loadFields() {
        // Name
        registerString("name", makeBukkitGetter(Entity::getName));
        // Locations
        registerLocation("location", SpellEntity::getLocation);
        registerLocation("position", SpellEntity::getLocation);
        registerLocation("eye_location", SpellEntity::getEyeLocation);
        registerLocation("eye_position", SpellEntity::getEyeLocation);
        registerNumber("x", e -> e.getLocation().getX());
        registerNumber("y", e -> e.getLocation().getY());
        registerNumber("z", e -> e.getLocation().getZ());
        registerNumber("yaw", e -> e.getLocation().getYaw());
        registerNumber("pitch", e -> e.getLocation().getPitch());
        // Health
        registerNumber("health", makeBukkitGetter(Damageable::getHealth, 0));
        // Attributes
        registerNumber("max_health", makeAttribute(Attribute.GENERIC_MAX_HEALTH));
        registerNumber("armor", makeAttribute(Attribute.GENERIC_ARMOR));
        registerNumber("armor_toughness", makeAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS));
        registerNumber("attack", makeAttribute(Attribute.GENERIC_ATTACK_DAMAGE));
        registerNumber("attack_damage", makeAttribute(Attribute.GENERIC_ATTACK_DAMAGE));
        registerNumber("damage", makeAttribute(Attribute.GENERIC_ATTACK_DAMAGE));
        registerNumber("speed", makeAttribute(Attribute.GENERIC_MOVEMENT_SPEED));
        registerNumber("knockback", makeAttribute(Attribute.GENERIC_ATTACK_KNOCKBACK));
        registerNumber("knockback_resistance", makeAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE));
    }

    private void loadFunctions() {
        registerNullFunc(
                "teleport",
                (se,a) -> se.teleport((Location) a.getFirst()),
                FunctionArgument.of(TypePrimitive.LOCATION)
        );
        registerNullFunc(
                "send_message",
                (se,a) -> se.sendMessage(StringTransformation.parse((String) a.getFirst())),
                FunctionArgument.of(TypePrimitive.STRING)
        );
        registerNullFunc(
                "heal",
                (se,a) -> se.getEntityAs(LivingEntity.class).ifPresent(le -> le.heal((Double)a.getFirst())),
                FunctionArgument.of(TypePrimitive.NUMBER)
        );
    }

    @Override
    protected @NotNull StructDefinition computeDsl() {
        return Objects.requireNonNull(ObjectsDefinitionRegistry.getDefaultStruct(getName()), "Entity struct cannot be found in defaults.");
    }

    private static <T> @NotNull Function<SpellEntity, T> makeBukkitGetter(@NotNull Function<LivingEntity, T> getter) {
        return makeBukkitGetter(getter, null);
    }

    private static <T> @NotNull Function<SpellEntity, T> makeBukkitGetter(@NotNull Function<LivingEntity, T> getter, @Nullable T defaultValue) {
        return se -> {
            if(se.getBukkitEntity().orElse(null) instanceof LivingEntity le)
                return getter.apply(le);
            return defaultValue;
        };
    }

    private static @NotNull Function<SpellEntity, Number> makeAttribute(@NotNull Attribute attribute) {
        return makeBukkitGetter(e -> {
            AttributeInstance ai = e.getAttribute(attribute);
            return ai == null ? 0 : ai.getValue();
        });
    }
}
