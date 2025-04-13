package fr.jamailun.ultimatespellsystem.plugin.bind;

import fr.jamailun.ultimatespellsystem.api.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.api.bind.ItemBindTrigger;
import fr.jamailun.ultimatespellsystem.api.bind.SpellBindData;
import fr.jamailun.ultimatespellsystem.api.bind.SpellCost;
import fr.jamailun.ultimatespellsystem.api.bind.SpellTrigger;
import fr.jamailun.ultimatespellsystem.api.spells.Spell;
import fr.jamailun.ultimatespellsystem.plugin.bind.costs.ItemAmountSpellCost;
import fr.jamailun.ultimatespellsystem.plugin.bind.costs.NoneSpellCost;
import fr.jamailun.ultimatespellsystem.plugin.spells.NotFoundSpell;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

@Getter
public class LegacySpellBindData implements SpellBindData {

  private final Spell spell;
  private final SpellTrigger trigger;

  public LegacySpellBindData(String spellId, boolean destroyable) {
    this(Objects.requireNonNullElseGet(
        UltimateSpellSystem.getSpellsManager().getSpell(spellId),
        () -> {
          UltimateSpellSystem.logWarning("Unknown spell id for LEGACY bind '" + spellId + "'.");
          return new NotFoundSpell(spellId);
        }
      ), destroyable);
  }

  public LegacySpellBindData(Spell spell, boolean destroyable) {
    this.spell = spell;
    this.trigger = new LegacySpellTrigger(destroyable);
  }

  @Getter
  public static class LegacySpellTrigger implements SpellTrigger {
    private final SpellCost cost;

    public LegacySpellTrigger(boolean destroyable) {
      cost = destroyable ? new ItemAmountSpellCost(1) : new NoneSpellCost();
    }

    @Override
    public boolean isMonoStep() {
      return true;
    }

    @Override
    public @NotNull ItemBindTrigger getMonoTrigger() {
      return ItemBindTrigger.RIGHT_CLICK;
    }

    @Override
    public @NotNull List<ItemBindTrigger> getTriggersList() {
      return List.of(getMonoTrigger());
    }
  }

}
