package fr.jamailun.ultimatespellsystem.api.spells;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

/**
 * A global manager to access spells.
 */
public interface SpellsManager {

    /**
     * Reload all spells from files.
     */
    void reloadSpells();

    /**
     * Register a custom spell implementation.
     * @param spell the spell to use.
     * @return false if the spell is invalid or if the name already exists. True in case of success.
     */
    boolean registerSpell(@NotNull Spell spell);

    /**
     * Get all spell IDs.
     * @return a non-null <b>read-only copy</b> list of spells IDs.
     */
    @NotNull @Unmodifiable List<String> spellIds();

    /**
     * Get all spells.
     * @return a non-null <b>read-only copy</b> list of spells.
     */
    @NotNull @Unmodifiable List<Spell> spells();

    /**
     * Find a spell.
     * @param name the name of the spell to find.
     * @return null if nothing was found.
     * @see Spell#getName()
     */
    @Nullable Spell getSpell(@NotNull String name);

}
