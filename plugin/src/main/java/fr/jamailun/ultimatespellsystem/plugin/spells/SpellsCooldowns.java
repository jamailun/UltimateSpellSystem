package fr.jamailun.ultimatespellsystem.plugin.spells;

import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Duration;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SpellsCooldowns {
    private SpellsCooldowns() {}

    private static final Map<UUID, Map<String, Instant>> COOLDOWNS = new ConcurrentHashMap<>(128);

    public static boolean canCast(@NotNull UUID caster, @NotNull String spellId, @NotNull Duration duration) {
        Map<String, Instant> data = COOLDOWNS.computeIfAbsent(caster, x -> new HashMap<>());
        Instant next = data.get(spellId);
        if(next == null || Instant.now().isAfter(next)) {
            data.put(spellId, Instant.now().plus(duration.asJavaDuration()));
            return true;
        }
        return false;
    }

    public static void removeCaster(@NotNull UUID caster) {
        COOLDOWNS.remove(caster);
    }

}
