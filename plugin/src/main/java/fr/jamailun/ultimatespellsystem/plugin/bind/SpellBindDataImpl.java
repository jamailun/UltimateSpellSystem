package fr.jamailun.ultimatespellsystem.plugin.bind;

import fr.jamailun.ultimatespellsystem.api.bind.SpellBindData;
import fr.jamailun.ultimatespellsystem.api.bind.SpellTrigger;
import fr.jamailun.ultimatespellsystem.api.spells.Spell;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Duration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Implementation of a spell data.
 * @param spell the spell to cast.
 * @param trigger the trigger.
 * @param cooldown nullable cooldown.
 */
public record SpellBindDataImpl(@NotNull Spell spell, @NotNull SpellTrigger trigger, @Nullable Duration cooldown) implements SpellBindData {
  @Override
  public @NotNull Spell getSpell() {
    return spell;
  }

  @Override
  public @NotNull SpellTrigger getTrigger() {
    return trigger;
  }

  @Override
  public @Nullable Duration getCooldown() {
    return cooldown;
  }
}
