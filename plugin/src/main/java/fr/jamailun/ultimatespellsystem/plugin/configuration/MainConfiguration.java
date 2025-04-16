package fr.jamailun.ultimatespellsystem.plugin.configuration;

import fr.jamailun.ultimatespellsystem.api.bind.ItemBindTrigger;
import fr.jamailun.ultimatespellsystem.api.bind.SpellCost;
import org.jetbrains.annotations.NotNull;
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

  long getTickAggroSummons();

}
