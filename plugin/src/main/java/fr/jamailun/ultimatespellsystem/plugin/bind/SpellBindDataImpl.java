package fr.jamailun.ultimatespellsystem.plugin.bind;

import fr.jamailun.ultimatespellsystem.api.bind.SpellBindData;
import fr.jamailun.ultimatespellsystem.api.bind.SpellTrigger;
import fr.jamailun.ultimatespellsystem.api.spells.Spell;
import org.jetbrains.annotations.NotNull;

/**
 * Implementation of a spell data.
 * @param spell the spell to cast.
 * @param trigger the trigger.
 */
public record SpellBindDataImpl(@NotNull Spell spell, @NotNull SpellTrigger trigger) implements SpellBindData {
  @Override
  public @NotNull Spell getSpell() {
    return spell;
  }

  @Override
  public @NotNull SpellTrigger getTrigger() {
    return trigger;
  }
}
