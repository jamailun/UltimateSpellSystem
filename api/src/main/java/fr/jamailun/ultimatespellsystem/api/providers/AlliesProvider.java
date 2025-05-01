package fr.jamailun.ultimatespellsystem.api.providers;

import fr.jamailun.ultimatespellsystem.api.entities.SpellEntity;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;

/**
 * Grants way for other plugins to "link" two entities/players as "ally".
 */
public final class AlliesProvider extends UssProvider<AlliesProvider.AlliesCheck> {
    private AlliesProvider() {/* Private constructor. */}
    private static final AlliesProvider INSTANCE = new AlliesProvider();

    /**
     * Get the instance.
     * @return the non-null, existing instance.
     */
    public static @NotNull AlliesProvider instance() {
        return INSTANCE;
    }

    /**
     * Test if two entities, a caster and a target are allies.
     * @param caster the caster of a spell.
     * @param target the target of the spell / summon.
     * @return a non-null result ; {@link AlliesResult#IGNORE} by default.
     */
    public @NotNull AlliesResult testForAllies(@NotNull SpellEntity caster, @NotNull Entity target) {
        for(AlliesCheck check : getValues()) {
            AlliesResult result = check.test(caster, target);
            if(result != AlliesResult.IGNORE)
                return result;
        }
        return AlliesResult.IGNORE;
    }

    /**
     * Test if two entities, a caster and a target are allies.
     * @param caster the caster of a spell.
     * @param target the target of the spell.
     * @return a non-null result ; {@link AlliesResult#IGNORE} by default.
     */
    public @NotNull AlliesResult testForAllies(@NotNull SpellEntity caster, @NotNull SpellEntity target) {
        Entity targetEntity = target.getBukkitEntity().orElse(null);
        if(targetEntity == null) return AlliesResult.IGNORE;
        return testForAllies(caster, targetEntity);
    }

    /**
     * A check result.
     */
    public enum AlliesResult {
        /** The plugin knows for sure the tested entities are <b>allies</b>. No further check will be done. */
        ALLIES,

        /** The plugin knows for sure the tested entities are <b>enemies</b>. No further check will be done. */
        ENEMIES,

        /** The plugin cannot say if the tested entities are allies or enemies. */
        IGNORE
    }

    /**
     * A function, called to check if two "entities" : a caster and a targeted bukkit {@link Entity} are allies.
     * For instance, you may want two players in a "party" to not be able to hit each other.
     * <br/>
     * It's essentially a {@link BiFunction}.
     */
    public interface AlliesCheck {

        @NotNull AlliesResult test(@NotNull SpellEntity caster, @NotNull Entity target);

    }

}
