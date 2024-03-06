package fr.jamailun.ultimatespellsystem.bukkit.runner.nodes.functions;

import fr.jamailun.ultimatespellsystem.bukkit.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.bukkit.spells.SpellEntity;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Duration;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.PotionEffect;
import fr.jamailun.ultimatespellsystem.bukkit.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.bukkit.runner.RuntimeStatement;
import fr.jamailun.ultimatespellsystem.bukkit.runner.SpellRuntime;
import org.bukkit.potion.PotionEffectType;
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
    public void run(SpellRuntime runtime) {
        List<SpellEntity> targets = runtime.safeEvaluateAcceptsList(targetRef, SpellEntity.class);

        PotionEffect effect = runtime.safeEvaluate(effectRef, PotionEffect.class);
        Duration duration = runtime.safeEvaluate(durationRef, Duration.class);
        int durationTicks = (int) duration.toTicks();
        int power = 1;
        if(optPowerRef != null) {
            power = runtime.safeEvaluate(optPowerRef, Double.class).intValue();
            if(power < 1) {
                power = 1;
                UltimateSpellSystem.logWarning("Invalid power value to effect " + effect);
            }
        }
        org.bukkit.potion.PotionEffect potionEffect = new org.bukkit.potion.PotionEffect(convertEffect(effect), durationTicks, power - 1);

        targets.forEach(e -> e.addPotionEffect(potionEffect));
    }

    public static PotionEffectType convertEffect(PotionEffect effect) {
        return switch (effect) {
            case SPEED -> PotionEffectType.SPEED;
            case SLOW -> PotionEffectType.SLOW;
            case HASTE -> PotionEffectType.FAST_DIGGING;
            case FATIGUE -> PotionEffectType.SLOW_DIGGING;
            case STRENGTH -> PotionEffectType.INCREASE_DAMAGE;
            case INSTANT_HEALTH -> PotionEffectType.HEAL;
            case INSTANT_DAMAGE -> PotionEffectType.HARM;
            case JUMP_BOOST -> PotionEffectType.JUMP;
            case NAUSEA -> PotionEffectType.CONFUSION;
            case REGENERATION -> PotionEffectType.REGENERATION;
            case RESISTANCE -> PotionEffectType.DAMAGE_RESISTANCE;
            case FIRE_RESISTANCE -> PotionEffectType.FIRE_RESISTANCE;
            case WATER_BREATHING -> PotionEffectType.WATER_BREATHING;
            case INVISIBILITY -> PotionEffectType.INVISIBILITY;
            case BLINDNESS -> PotionEffectType.BLINDNESS;
            case NIGHT_VISION -> PotionEffectType.NIGHT_VISION;
            case HUNGER -> PotionEffectType.HUNGER;
            case WEAKNESS -> PotionEffectType.WEAKNESS;
            case POISON -> PotionEffectType.POISON;
            case WITHER -> PotionEffectType.WITHER;
            case HEALTH_BOOST -> PotionEffectType.HEALTH_BOOST;
            case ABSORPTION -> PotionEffectType.ABSORPTION;
            case SATURATION -> PotionEffectType.SATURATION;
            case GLOWING -> PotionEffectType.GLOWING;
            case LEVITATION -> PotionEffectType.LEVITATION;
            case LUCK -> PotionEffectType.LUCK;
            case BAD_LUCK -> PotionEffectType.UNLUCK;
            case SLOW_FALLING -> PotionEffectType.SLOW_FALLING;
            case CONDUIT_POWER -> PotionEffectType.CONDUIT_POWER;
            case DOLPHIN_GRACE -> PotionEffectType.DOLPHINS_GRACE;
            case BAD_OMEN -> PotionEffectType.BAD_OMEN;
            case HERO_OF_THE_VILLAGE -> PotionEffectType.HERO_OF_THE_VILLAGE;
            case DARKNESS -> PotionEffectType.DARKNESS;
        };
    }
}
