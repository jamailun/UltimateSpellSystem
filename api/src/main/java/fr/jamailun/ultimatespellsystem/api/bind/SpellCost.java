package fr.jamailun.ultimatespellsystem.api.bind;

import fr.jamailun.ultimatespellsystem.api.entities.SpellEntity;
import org.jetbrains.annotations.NotNull;

/**
 * The cost of a {@link fr.jamailun.ultimatespellsystem.api.spells.Spell Spell}.
 */
public interface SpellCost {

    /**
     * Test if a spell-entity, as a caster, can afford to trigger the spell in this instant.
     * @param caster the non-null potential caster.
     * @return tru if the spell can be afforded.
     */
    boolean canPay(@NotNull SpellEntity caster);

    /**
     * MAke a caster pay the cost of this spell.
     * @param caster the caster.
     */
    void pay(@NotNull SpellEntity caster);

    /**
     * Serialize this object.
     * @return a non null string.
     */
    @NotNull String serialize();

}
