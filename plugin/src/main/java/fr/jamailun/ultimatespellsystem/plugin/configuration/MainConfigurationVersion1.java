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
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.List;
import java.util.Objects;

/**
 * Version {@code 1.2} of configuration file.
 */
@Configuration
public class MainConfigurationVersion1 implements MainConfiguration {

  @Comment("Dont change this value manually. It does not match the plugin version.")
  @Getter @Setter private String version = "1.3";

  @Comment({"","If true, 'debug' log will be printed into the console."})
  @Getter private boolean debug = false;

  @Comment({"","Default values of the bind command."})
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

  @Comment({"","Tick-rate management"})
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

  @Comment({"","Bound-spell behaviour."})
  private BindSpellSection bindSpell = new BindSpellSection(
          true,
          true,
          null
  );

  private record BindSpellSection(
          @Comment({
                  "If true, all cast spells will cancel their last action.",
                  "For example, a RIGHT_CLICK bind in front of a lever will NOT trigger it."
          })
          boolean cancelOnCast,
          @Comment({
                  "If true, all bind-steps (not resulting in a cast, but valid step anyway) will cancel",
                  "their event, as defined in the previous comment."
          })
          boolean cancelOnStep,
          @Comment({"Cooldown settings (only apply to bound spells)"})
          CooldownSection cooldown
  ) {}

  private record CooldownSection(
    @Comment({"If true, the player will have the cooldown applied to the MATERIAL (like an ender-pearl for example)."})
    boolean onMaterial,
    @Comment({"Message to send to a player if he tries to cast a spell in-cooldown.", "If empty, will not send anything."})
    String tooQuickMessage
  ) {}

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

  @Override
  public boolean cancelOnStep() {
    return bindSpell.cancelOnStep();
  }

  @Override
  public boolean cancelOnCast() {
    return bindSpell.cancelOnCast();
  }

  @Override
  public @NotNull String messageOnCooldown() {
    return Objects.requireNonNullElse(bindSpell.cooldown().tooQuickMessage(), "");
  }

  @Override
  public boolean addCooldownToMaterial() {
    return bindSpell.cooldown().onMaterial();
  }

  public void checkDefaults() {
    // cooldown ?
    UssLogger.logDebug("bid-spell = " + bindSpell);
    if(bindSpell.cooldown() == null) {
      bindSpell = new BindSpellSection(
              bindSpell.cancelOnCast(),
              bindSpell.cancelOnStep(),
              new CooldownSection(true, "&cToo quick! This spell is still on cooldown.")
      );
    }
  }

}
