package fr.jamailun.ultimatespellsystem.dsl.nodes.type;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum PotionEffect {
    SPEED(),
    SLOW("slowness"),
    HASTE("mining_speed", "fast_digging"),
    FATIGUE("slow_mining", "mining_fatigue", "slow_digging"),
    STRENGTH("force"),
    INSTANT_HEALTH("heal", "health", "instant_heal"),
    INSTANT_DAMAGE("damage", "harm"),
    JUMP_BOOST(),
    NAUSEA("confusion"),
    REGENERATION("regen"),
    RESISTANCE(),
    FIRE_RESISTANCE(),
    WATER_BREATHING("respiration", "apnea", "water_respiration", "breathing"),
    INVISIBILITY(),
    BLINDNESS("blind"),
    NIGHT_VISION(),
    HUNGER("hungary"), // I know right: I'm funny
    WEAKNESS("weak"),
    POISON(),
    WITHER("wither_effect"),
    HEALTH_BOOST(),
    ABSORPTION(),
    SATURATION(),
    GLOWING(),
    LEVITATION(),
    LUCK(),
    BAD_LUCK("bad_luck"),
    SLOW_FALLING(),
    CONDUIT_POWER("conduit"),
    DOLPHIN_GRACE("dolphin_power"),
    BAD_OMEN(),
    HERO_OF_THE_VILLAGE("hero"),
    DARKNESS("dark");

    private final List<String> identifiers;

    PotionEffect(String... identifiers) {
        this.identifiers = new ArrayList<>(List.of(identifiers));
        this.identifiers.add(name().toLowerCase());
    }

    public static PotionEffect find(@NotNull String identifier) {
        String identifierLC = identifier.toLowerCase();
        return Arrays.stream(values())
                .filter(p -> p.identifiers.contains(identifierLC))
                .findFirst()
                .orElse(null);
    }
}