package fr.jamailun.ultimatespellsystem.plugin.bind;

import fr.jamailun.ultimatespellsystem.api.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.api.bind.ItemBindTrigger;
import fr.jamailun.ultimatespellsystem.api.bind.SpellBindData;
import fr.jamailun.ultimatespellsystem.api.bind.SpellCost;
import fr.jamailun.ultimatespellsystem.api.bind.SpellTrigger;
import fr.jamailun.ultimatespellsystem.api.spells.Spell;
import fr.jamailun.ultimatespellsystem.plugin.bind.costs.SpellCostFactory;
import fr.jamailun.ultimatespellsystem.plugin.spells.NotFoundSpell;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public final class SpellBindFactory {

  public static @NotNull SpellBindData deserialize(@NotNull String data) {
    List<String> lines = new ArrayList<>(List.of(data.split(";")));
    if(lines.size() < 3) {
      throw new RuntimeException("Invalid data : expected at least 3 args.");
    }
    // Read spell
    String spellId = lines.getFirst();
    Spell spell = Objects.requireNonNullElseGet(UltimateSpellSystem.getSpellsManager().getSpell(spellId), () -> {
      UltimateSpellSystem.logWarning("Unknown spell id from SpellBindDataFactory: '" + spellId + "'.");
      return new NotFoundSpell(spellId);
    });

    // Read the rest
    lines.removeFirst();
    SpellTrigger trigger = deserializeTrigger(lines);
    return new SpellBindDataImpl(spell, trigger);
  }

  public static @NotNull String serialize(@NotNull SpellBindData data) {
    return data.getSpell().getName() + ";"
        + serialize(data.getTrigger()) + ";"
        + SpellCostFactory.serialize(data.getCost());
  }

  private static @NotNull String serialize(@NotNull SpellTrigger trigger) {
    return trigger.getTriggersList().stream().map(e -> String.valueOf(e.getCode())).collect(Collectors.joining(","));
  }

  private static @NotNull SpellTrigger deserializeTrigger(@NotNull List<String> data) {
    String triggerRaw = data.getFirst();
    data.removeFirst();
    SpellCost cost = SpellCostFactory.deserialize(triggerRaw);
    String[] triggerParts = triggerRaw.split(",");
    List<ItemBindTrigger> triggers = Arrays.stream(triggerParts)
        .mapToInt(Integer::valueOf)
        .mapToObj(ItemBindTrigger::fromCode)
        .toList();
    return new SpellTriggerImpl(triggers, cost);
  }

}
