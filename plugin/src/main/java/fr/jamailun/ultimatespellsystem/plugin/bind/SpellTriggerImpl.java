package fr.jamailun.ultimatespellsystem.plugin.bind;

import fr.jamailun.ultimatespellsystem.api.bind.ItemBindTrigger;
import fr.jamailun.ultimatespellsystem.api.bind.SpellCost;
import fr.jamailun.ultimatespellsystem.api.bind.SpellTrigger;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Basic implementation of a {@link SpellTrigger}.
 * @param triggers list of triggers.
 * @param cost the optional cost of this spell.
 */
public record SpellTriggerImpl(@NotNull List<ItemBindTrigger> triggers, @NotNull SpellCost cost) implements SpellTrigger {

  @Override
  public @NotNull List<ItemBindTrigger> getTriggersList() {
    return triggers;
  }

  @Override
  public @NotNull SpellCost getCost() {
    return cost;
  }
}
