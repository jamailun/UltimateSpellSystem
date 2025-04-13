package fr.jamailun.ultimatespellsystem.plugin.bind;

import fr.jamailun.ultimatespellsystem.api.bind.ItemBindTrigger;
import fr.jamailun.ultimatespellsystem.api.bind.SpellCost;
import fr.jamailun.ultimatespellsystem.api.bind.SpellTrigger;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record SpellTriggerImpl(@NotNull List<ItemBindTrigger> triggers, @NotNull SpellCost cost) implements SpellTrigger {

  @Override
  public boolean isMonoStep() {
    return triggers.size() == 1;
  }

  @Override
  public @NotNull ItemBindTrigger getMonoTrigger() {
    if(triggers.size() > 1)
      throw new IllegalStateException("This spellTrigger has multiple steps.");
    return triggers.getFirst();
  }

  @Override
  public @NotNull List<ItemBindTrigger> getTriggersList() {
    return triggers;
  }

  @Override
  public @NotNull SpellCost getCost() {
    return cost;
  }
}
