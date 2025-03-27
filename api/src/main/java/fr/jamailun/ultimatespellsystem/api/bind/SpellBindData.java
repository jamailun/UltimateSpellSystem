package fr.jamailun.ultimatespellsystem.api.bind;

import fr.jamailun.ultimatespellsystem.api.spells.Spell;
import org.jetbrains.annotations.NotNull;

/**
 * Bind data of a spell.
 */
public interface SpellBindData {

    @NotNull Spell getSpell();

    @NotNull SpellTrigger getTrigger();

    //xxx should i put the cost here ??

}
