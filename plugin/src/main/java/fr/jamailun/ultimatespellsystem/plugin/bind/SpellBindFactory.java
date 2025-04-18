package fr.jamailun.ultimatespellsystem.plugin.bind;

import fr.jamailun.ultimatespellsystem.UssLogger;
import fr.jamailun.ultimatespellsystem.api.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.api.bind.ItemBindTrigger;
import fr.jamailun.ultimatespellsystem.api.bind.SpellBindData;
import fr.jamailun.ultimatespellsystem.api.bind.SpellCost;
import fr.jamailun.ultimatespellsystem.api.bind.SpellTrigger;
import fr.jamailun.ultimatespellsystem.api.spells.Spell;
import fr.jamailun.ultimatespellsystem.plugin.bind.costs.SpellCostFactory;
import fr.jamailun.ultimatespellsystem.plugin.spells.NotFoundSpell;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public final class SpellBindFactory {

  public static @NotNull SpellBindDataContainer deserializeContainer(@NotNull String serializedData) {
    if(serializedData.isEmpty()) {
      return new SpellBindDataContainer(Collections.emptyList());
    }
    String[] base64Parts = serializedData.split("\\.");
    List<SpellBindData> list = new ArrayList<>();
    for(String base64Part : base64Parts) {
      byte[] raw = Base64.getDecoder().decode(base64Part);
      String decoded = new String(raw, StandardCharsets.UTF_8);
      list.add(deserialize(decoded));
    }
    return new SpellBindDataContainer(list);
  }

  public static @NotNull SpellBindData deserialize(@NotNull String data) {
    List<String> lines = new ArrayList<>(List.of(data.split(";")));
    if(lines.size() < 3) {
      throw new RuntimeException("Invalid data : expected at least 3 args.");
    }
    // Read spell
    String spellId = lines.getFirst();
    Spell spell = Objects.requireNonNullElseGet(UltimateSpellSystem.getSpellsManager().getSpell(spellId), () -> {
      UssLogger.logWarning("Unknown spell id from SpellBindDataFactory: '" + spellId + "'.");
      return new NotFoundSpell(spellId);
    });

    // Read the rest
    lines.removeFirst();
    SpellTrigger trigger = deserializeTrigger(lines);
    return new SpellBindDataImpl(spell, trigger);
  }

  public static @NotNull String serialize(@NotNull SpellBindDataContainer container) {
    StringJoiner joiner = new StringJoiner(".");
    for(SpellBindData data : container.list()) {
      String raw = serialize(data);
      String base64 = Base64.getEncoder().encodeToString(raw.getBytes(StandardCharsets.UTF_8));
      joiner.add(base64);
    }
    return joiner.toString();
  }

  public static @NotNull String serialize(@NotNull SpellBindData data) {
    return data.getSpell().getName() + ";"
        + serialize(data.getTrigger()) + ";"
        + SpellCostFactory.serialize(data.getCost());
  }

  private static @NotNull String serialize(@NotNull SpellTrigger trigger) {
    return trigger.getTriggersList().stream()
        .map(Enum::name)
        .collect(Collectors.joining(","));
  }

  private static @NotNull SpellTrigger deserializeTrigger(@NotNull List<String> data) {
    String triggerRaw = data.getFirst();
    data.removeFirst();
    String[] triggerParts = triggerRaw.split(",");
    List<ItemBindTrigger> triggers = Arrays.stream(triggerParts)
        .map(ItemBindTrigger::valueOf)
        .toList();

    SpellCost cost = SpellCostFactory.deserialize(data);
    return new SpellTriggerImpl(triggers, cost);
  }

}
