package fr.jamailun.ultimatespellsystem.bukkit.entities.implem;

import fr.jamailun.ultimatespellsystem.bukkit.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.bukkit.runner.nodes.functions.SendEffectNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Duration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;

import java.util.Collection;
import java.util.Map;

/**
 * Represents one potion effect, given by an orb.
 */
public class OrbEffect {

    private final PotionEffect builtEffect;

    OrbEffect(fr.jamailun.ultimatespellsystem.dsl.nodes.type.PotionEffect effect, Duration duration, int power) {
        builtEffect = new PotionEffect(SendEffectNode.convertEffect(effect), (int) duration.toTicks(), power - 1);
        UltimateSpellSystem.logDebug("New orb-effect : (" + builtEffect + ")");
    }

    /**
     * Build an OrbEffect from a map.
     * @param context the debug location of the attributes, used for printing-purpose.
     * @param values the map of attributes. Expected keys: {type, duration, power}
     * @return null if an error occurred.
     */
    public static OrbEffect build(String context, Map<?, ?> values) {
        // Type
        Object typeRaw = values.get("type");
        fr.jamailun.ultimatespellsystem.dsl.nodes.type.PotionEffect effect;
        if(typeRaw instanceof fr.jamailun.ultimatespellsystem.dsl.nodes.type.PotionEffect pe) {
            effect = pe;
        } else if(typeRaw instanceof String type) {
            effect = fr.jamailun.ultimatespellsystem.dsl.nodes.type.PotionEffect.find(type);
            if(effect == null) {
                UltimateSpellSystem.logError("(" + context + ") Unknown effect-type : '" + type + "'.");
                return null;
            }
        } else {
            UltimateSpellSystem.logError("(" + context + ") Invalid effect type : '" + typeRaw + "'.");
            return null;
        }

        // Duration
        Object durRaw = values.get("duration");
        if(!(durRaw instanceof Duration duration)) {
            UltimateSpellSystem.logError("(" + context + ") Invalid duration type : '" + durRaw + "'.");
            return null;
        }

        // Power
        if(!values.containsKey("power")) {
            return new OrbEffect(effect, duration, 1);
        }

        Object powRaw = values.get("power");
        if(!(powRaw instanceof Double power)) {
            UltimateSpellSystem.logError("(" + context + ") Invalid duration type : '" + durRaw + "'.");
            return null;
        }
        return new OrbEffect(effect, duration, power.intValue());
    }

    /**
     * Apply the potion effects to a collection of entities.
     * @param entities a non-null collection.
     */
    public void apply(Collection<LivingEntity> entities) {
        entities.forEach(e -> e.addPotionEffect(builtEffect));
    }

}
