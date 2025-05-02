package fr.jamailun.ultimatespellsystem.plugin.bind.trigger;

import fr.jamailun.ultimatespellsystem.api.bind.ItemBindTrigger;
import fr.jamailun.ultimatespellsystem.api.bind.SpellBindData;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Stores actions of a player.
 */
public class SpellTriggerSession {

  private static final Duration MAX_DURATION = Duration.ofSeconds(2);

  @Getter private final Player player;
  @Getter private final ItemStack item;

  private Instant lastUpdate;
  private final List<ItemBindTrigger> actions = new ArrayList<>();
  private final List<SpellBindData> spells;
  boolean isValid = true;

  public SpellTriggerSession(@NotNull Player player, @NotNull ItemStack item, @NotNull List<SpellBindData> data) {
    this.player = player;
    this.item = item;

    lastUpdate = Instant.now();
    spells = new ArrayList<>(data);
  }

  /**
   * Propagate an action. Will filter allowed spells.
   * @param action the propagated action.
   * @return an optional with the caster spell value, if exists.
   */
  public @NotNull ActionRes action(@NotNull ItemBindTrigger action) {
    // Remove non-matching spells.
    Iterator<SpellBindData> iterator = spells.listIterator();
    boolean foundOne = false;
    while(iterator.hasNext()) {
      SpellBindData spell = iterator.next();
      if(accepts(spell, action, actions.size())) {
        foundOne = true;
      } else {
        iterator.remove();
      }
    }
    // This action does not match anything : returns null.
    if(!foundOne || spells.isEmpty()) {
      isValid = false;
      return ActionRes.ignored();
    }

    actions.add(action);
    lastUpdate = Instant.now();

    // Is casting over ?
    if(spells.size() == 1 && spells.getFirst().getTrigger().getTriggersList().size() == actions.size()) {
      return ActionRes.success(spells.getFirst());
    }
    return ActionRes.okWithoutSpell();
  }

  public boolean isTooOldOrInvalid() {
    return !isValid || Instant.now().isAfter(lastUpdate.plus(MAX_DURATION));
  }

  public @NotNull ItemBindTrigger getLastAction() {
    return actions.getLast();
  }

  private static boolean accepts(@NotNull SpellBindData data, @NotNull ItemBindTrigger action, int index) {
    var steps = data.getTrigger().getTriggersList();
    return index < steps.size() && steps.get(index).matches(action);
  }

}
