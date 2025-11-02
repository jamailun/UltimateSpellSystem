package fr.jamailun.ultimatespellsystem.plugin.spells;

import fr.jamailun.ultimatespellsystem.dsl2.nodes.type.Duration;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Static handler for cooldowns.
 */
public final class SpellsCooldowns {
    private SpellsCooldowns() {}

    private static final Map<UUID, Map<String, Instant>> COOLDOWNS = new ConcurrentHashMap<>(128);

    /**
     * Test if an entity can cast.
     * @param caster UUID of the potential caster.
     * @param spellId ID of the spell to use.
     * @param duration cooldown duration to apply on the next cooldown.
     * @return true if the caster can cast this specific spell at this instant.
     */
    public static boolean canCast(@NotNull UUID caster, @NotNull String spellId, @NotNull Duration duration) {
        Map<String, Instant> data = COOLDOWNS.computeIfAbsent(caster, x -> new HashMap<>());
        Instant next = data.get(spellId);
        if(next == null || Instant.now().isAfter(next)) {
            data.put(spellId, Instant.now().plus(duration.asJavaDuration()));
            return true;
        }
        return false;
    }

    /**
     * Clear the data related to one caster.
     * @param caster UUID of the caster to clear the memory of.
     */
    public static void removeCaster(@NotNull UUID caster) {
        COOLDOWNS.remove(caster);
    }

}
