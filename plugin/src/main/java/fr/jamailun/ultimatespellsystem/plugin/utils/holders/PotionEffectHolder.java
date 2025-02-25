package fr.jamailun.ultimatespellsystem.plugin.utils.holders;

import fr.jamailun.ultimatespellsystem.api.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.api.runner.errors.InvalidEnumValueException;
import fr.jamailun.ultimatespellsystem.api.runner.errors.InvalidTypeException;
import fr.jamailun.ultimatespellsystem.plugin.runner.nodes.functions.SendEffectNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Duration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;

/**
 * Holds a potion effect.
 */
public class PotionEffectHolder {

    private final PotionEffect builtEffect;

    public PotionEffectHolder(@NotNull String effectRaw, @NotNull Duration duration, int power) {
        PotionEffectType type = SendEffectNode.convertEffect(effectRaw);
        if(type == null)
            throw new InvalidEnumValueException(PotionEffectType.class, effectRaw);
        builtEffect = new PotionEffect(type, (int) duration.toTicks(), power - 1);
    }

    /**
     * Build a PotionEffectHolder from a map.
     * @param context the debug location of the attributes, used for printing-purpose.
     * @param values the map of attributes. Expected keys: {type, duration, power}
     * @return null if an error occurred.
     */
    public static @Nullable PotionEffectHolder build(String context, @NotNull Map<?, ?> values) {
        // Type
        Object typeRaw = values.get("type");
        if(!(typeRaw instanceof String effect)) {
            throw new InvalidTypeException("building potion-effect-holder (effect type)", "String", typeRaw);
        }

        // Duration
        Object durRaw = values.get("duration");
        if(!(durRaw instanceof Duration duration)) {
            UltimateSpellSystem.logError("(" + context + ") Invalid duration type : '" + durRaw + "'.");
            return null;
        }

        // Power
        if(!values.containsKey("power")) {
            return new PotionEffectHolder(effect, duration, 1);
        }

        Object powRaw = values.get("power");
        if(!(powRaw instanceof Double power)) {
            UltimateSpellSystem.logError("(" + context + ") Invalid duration type : '" + durRaw + "'.");
            return null;
        }
        return new PotionEffectHolder(effect, duration, power.intValue());
    }

    /**
     * Apply the potion effects to a collection of entities.
     * @param entities a non-null collection.
     */
    public void apply(@NotNull Collection<LivingEntity> entities) {
        entities.forEach(e -> e.addPotionEffect(builtEffect));
    }

    /**
     * Apply the potion effects to a single entity.
     * @param entity a non-null entity.
     */
    public void apply(@NotNull LivingEntity entity) {
        entity.addPotionEffect(builtEffect);
    }

}
