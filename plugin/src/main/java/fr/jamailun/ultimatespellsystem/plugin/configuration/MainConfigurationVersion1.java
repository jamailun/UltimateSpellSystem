package fr.jamailun.ultimatespellsystem.plugin.configuration;

import de.exlll.configlib.Comment;
import de.exlll.configlib.Configuration;
import fr.jamailun.ultimatespellsystem.UssLogger;
import fr.jamailun.ultimatespellsystem.api.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.api.bind.ItemBindTrigger;
import fr.jamailun.ultimatespellsystem.api.bind.SpellCost;
import fr.jamailun.ultimatespellsystem.api.bind.SpellCostEntry;
import fr.jamailun.ultimatespellsystem.plugin.bind.costs.NoneSpellCost;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.List;

/**
 * Version {@code 1.1} of configuration file.
 */
@Configuration
public class MainConfigurationVersion1 implements MainConfiguration {

  @Comment("Dont change this value manually.")
  @Getter private String version = "1.1";

  @Comment("If true, 'debug' log will be printed into the console.")
  @Getter private boolean debug = false;

  @Comment("Default values of the bind command.")
  private SectionDefault defaultSection = new SectionDefault(
      List.of(ItemBindTrigger.RIGHT_CLICK),
      new DefaultSpellCostType("none", List.of())
  );
  private record SectionDefault(
      @Comment("Default trigger sequence, if not set by the admin.")
      List<ItemBindTrigger> trigger,
      @Comment("Default cost, if not set by the admin.")
      DefaultSpellCostType cost
  ) {}
  private record DefaultSpellCostType(
      String type,
      List<String> args
  ) {
    public SpellCost deserialize() {
      SpellCostEntry<?> entry = UltimateSpellSystem.getSpellCostRegistry().get(type);
      if(entry == null) {
        UssLogger.logWarning("Configuration: Unknown spell cost '" + type + "'.");
        return new NoneSpellCost();
      }
      try {
        return entry.deserialize(args);
      } catch(Exception e) {
        UssLogger.logWarning("Configuration: Could not deserialize spell cost '" + type + "' with args " + args + " : " + e.getMessage());
        return new NoneSpellCost();
      }
    }
  }

  @Comment("Tick-rate management")
  private TickSection tick = new TickSection(new TickSection.TickAggroSection(5));
  private record TickSection(
      @Comment("Tick-rate related to aggro management")
      TickAggroSection aggro
  ) {
    private record TickAggroSection(
        @Comment("Update of aggro for summons.")
        long summons
    ) {}
  }

  // -- read methods

  @Override
  public @NotNull @UnmodifiableView List<ItemBindTrigger> getDefaultTriggerSteps() {
    return defaultSection.trigger();
  }

  @Override
  public @NotNull SpellCost getDefaultSpellCost() {
    return defaultSection.cost().deserialize();
  }

  @Override
  public long getTickAggroSummons() {
    return tick.aggro().summons();
  }
}
