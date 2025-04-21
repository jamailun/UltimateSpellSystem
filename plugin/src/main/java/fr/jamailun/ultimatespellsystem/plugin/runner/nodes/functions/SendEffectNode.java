package fr.jamailun.ultimatespellsystem.plugin.runner.nodes.functions;

import fr.jamailun.ultimatespellsystem.UssLogger;
import fr.jamailun.ultimatespellsystem.api.entities.SpellEntity;
import fr.jamailun.ultimatespellsystem.api.runner.errors.InvalidEnumValueException;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Duration;
import fr.jamailun.ultimatespellsystem.api.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.api.runner.RuntimeStatement;
import fr.jamailun.ultimatespellsystem.api.runner.SpellRuntime;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SendEffectNode extends RuntimeStatement {

    private final RuntimeExpression targetRef;
    private final RuntimeExpression effectRef;
    private final RuntimeExpression durationRef;
    private final RuntimeExpression optPowerRef;

    public SendEffectNode(RuntimeExpression target, RuntimeExpression effectRef, RuntimeExpression durationRef, @Nullable RuntimeExpression optPowerRef) {
        this.targetRef = target;
        this.effectRef = effectRef;
        this.durationRef = durationRef;
        this.optPowerRef = optPowerRef;
    }

    @Override
    public void run(@NotNull SpellRuntime runtime) {
        List<SpellEntity> targets = runtime.safeEvaluateAcceptsList(targetRef, SpellEntity.class);

        String effectRaw = runtime.safeEvaluate(effectRef, String.class);
        PotionEffectType effectType = convertEffect(effectRaw);
        if(effectType == null)
            throw new InvalidEnumValueException(PotionEffectType.class, effectRaw);

        Duration duration = runtime.safeEvaluate(durationRef, Duration.class);
        int durationTicks = (int) duration.toTicks();
        int power = 1;
        if(optPowerRef != null) {
            power = runtime.safeEvaluate(optPowerRef, Double.class).intValue();
            if(power < 1) {
                power = 1;
                UssLogger.logWarning("Invalid power value to effect " + effectRaw);
            }
        }
        PotionEffect potionEffect = new PotionEffect(effectType, durationTicks, power - 1);

        targets.forEach(e -> e.addPotionEffect(potionEffect));
    }

    public static @Nullable PotionEffectType convertEffect(@NotNull String effectRaw) {
        return switch (effectRaw.toLowerCase()) {
            case "speed" -> PotionEffectType.SPEED;
            case "slow", "slowness" -> PotionEffectType.SLOWNESS;
            case "hast", "mining_speed", "fast_digging" -> PotionEffectType.HASTE;
            case "fatigue", "slow_mining", "mining_fatigue", "slow_digging" -> PotionEffectType.MINING_FATIGUE;
            case "strength", "force" -> PotionEffectType.STRENGTH;
            case "instant_health", "instant_heal", "heal","health" -> PotionEffectType.INSTANT_HEALTH;
            case "instant_damage", "damage", "harm" -> PotionEffectType.INSTANT_DAMAGE;
            case "jump_boost", "jumpness" -> PotionEffectType.JUMP_BOOST;
            case "nausea", "confusion" -> PotionEffectType.NAUSEA;
            case "regeneration", "regen" -> PotionEffectType.REGENERATION;
            case "resistance", "defense" -> PotionEffectType.RESISTANCE;
            case "fire_resistance" -> PotionEffectType.FIRE_RESISTANCE;
            case "water_breathing", "respiration", "apnea", "water_respiration", "breathing" -> PotionEffectType.WATER_BREATHING;
            case "invisibility", "transparency" -> PotionEffectType.INVISIBILITY;
            case "blindness", "blind" -> PotionEffectType.BLINDNESS;
            case "night_vision" -> PotionEffectType.NIGHT_VISION;
            case "hunger", "hungary" /* Where's Hungary? */ -> PotionEffectType.HUNGER;
            case "weakness", "weak" -> PotionEffectType.WEAKNESS;
            case "poison" -> PotionEffectType.POISON;
            case "wither" -> PotionEffectType.WITHER;
            case "health_boost", "max_health" -> PotionEffectType.HEALTH_BOOST;
            case "absorption", "bonus_health", "health_bonus" -> PotionEffectType.ABSORPTION;
            case "saturation" -> PotionEffectType.SATURATION;
            case "glowing", "glow", "glowness" -> PotionEffectType.GLOWING;
            case "levitation" -> PotionEffectType.LEVITATION;
            case "luck" -> PotionEffectType.LUCK;
            case "bad_luck" -> PotionEffectType.UNLUCK;
            case "slow_falling" -> PotionEffectType.SLOW_FALLING;
            case "conduit_power", "conduit" -> PotionEffectType.CONDUIT_POWER;
            case "dolphin_grace", "dolphin_power" -> PotionEffectType.DOLPHINS_GRACE;
            case "bad_omen" -> PotionEffectType.BAD_OMEN;
            case "hero_of_the_village", "hero" -> PotionEffectType.HERO_OF_THE_VILLAGE;
            case "darkness", "dark" -> PotionEffectType.DARKNESS;
            default -> null;
        };
    }
}
