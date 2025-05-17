package fr.jamailun.ultimatespellsystem.plugin.configuration;

import fr.jamailun.ultimatespellsystem.api.bind.ItemBindTrigger;
import fr.jamailun.ultimatespellsystem.api.bind.SpellCost;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Duration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.List;

/**
 * Interface for USS configuration reference.
 */
public interface MainConfiguration {

  @NotNull String getVersion();

  boolean isDebug();

  @NotNull @UnmodifiableView List<ItemBindTrigger> getDefaultTriggerSteps();
  @NotNull SpellCost getDefaultSpellCost();
  @Nullable Duration getDefaultCooldown();

  long getTickAggroSummons();
  int getTickDefaultCustomEntity();

  boolean cancelOnStep();
  boolean cancelOnCast();

  @NotNull String messageOnCooldown();

  boolean addCooldownToMaterial();

  boolean displaySummonWarnings();

}
