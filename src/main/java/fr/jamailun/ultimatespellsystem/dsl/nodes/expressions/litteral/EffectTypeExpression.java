package fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.litteral;

import fr.jamailun.ultimatespellsystem.dsl.errors.ParsingException;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Type;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenPosition;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EffectTypeExpression extends LiteralExpression {

    private final PotionEffect effect;

    public EffectTypeExpression(TokenPosition position, PotionEffect effect) {
        super(position);
        this.effect = effect;
    }

    public PotionEffect getRawEffect() {
        return effect;
    }

    @Override
    public Type getExpressionType() {
        return TypePrimitive.EFFECT_TYPE.asType();
    }

    @Override
    public String toString() {
        return "EffectType." + effect;
    }

    public enum PotionEffect {
        SPEED(PotionEffectType.SPEED),
        SLOW(PotionEffectType.SLOW, "slowness"),
        HASTE(PotionEffectType.FAST_DIGGING, "mining_speed", "fast_digging"),
        FATIGUE(PotionEffectType.SLOW_DIGGING, "slow_mining", "mining_fatigue", "slow_digging"),
        STRENGTH(PotionEffectType.INCREASE_DAMAGE, "force"),
        INSTANT_HEALTH(PotionEffectType.HEAL, "heal", "health", "instant_heal"),
        INSTANT_DAMAGE(PotionEffectType.HARM, "damage", "harm"),
        JUMP_BOOST(PotionEffectType.JUMP),
        NAUSEA(PotionEffectType.CONFUSION, "confusion"),
        REGENERATION(PotionEffectType.REGENERATION, "regen"),
        RESISTANCE(PotionEffectType.DAMAGE_RESISTANCE),
        FIRE_RESISTANCE(PotionEffectType.FIRE_RESISTANCE),
        WATER_BREATHING(PotionEffectType.WATER_BREATHING, "respiration", "apnea", "water_respiration", "breathing"),
        INVISIBILITY(PotionEffectType.INVISIBILITY),
        BLINDNESS(PotionEffectType.BLINDNESS, "blind"),
        NIGHT_VISION(PotionEffectType.NIGHT_VISION),
        HUNGER(PotionEffectType.HUNGER, "hungary"), // I know right: I'm funny
        WEAKNESS(PotionEffectType.WEAKNESS, "weak"),
        POISON(PotionEffectType.POISON),
        WITHER(PotionEffectType.WITHER, "wither_effect"),
        HEALTH_BOOST(PotionEffectType.HEALTH_BOOST),
        ABSORPTION(PotionEffectType.ABSORPTION),
        SATURATION(PotionEffectType.SATURATION),
        GLOWING(PotionEffectType.GLOWING),
        LEVITATION(PotionEffectType.LEVITATION),
        LUCK(PotionEffectType.LUCK),
        BAD_LUCK(PotionEffectType.UNLUCK, "bad_luck"),
        SLOW_FALLING(PotionEffectType.SLOW_FALLING),
        CONDUIT_POWER(PotionEffectType.CONDUIT_POWER, "conduit"),
        DOLPHIN_GRACE(PotionEffectType.DOLPHINS_GRACE, "dolphin_power"),
        BAD_OMEN(PotionEffectType.BAD_OMEN),
        HERO_OF_THE_VILLAGE(PotionEffectType.HERO_OF_THE_VILLAGE, "hero"),
        DARKNESS(PotionEffectType.DARKNESS, "dark");

        private final List<String> identifiers;

        PotionEffect(PotionEffectType bukkitEffect, String... identifiers) {
            this.identifiers = new ArrayList<>(List.of(identifiers));
            this.identifiers.add(name().toLowerCase());
        }

        public static PotionEffect find(String identifier) {
            return Arrays.stream(values())
                    .filter(p -> p.identifiers.contains(identifier))
                    .findFirst()
                    .orElse(null);
        }
    }

}
