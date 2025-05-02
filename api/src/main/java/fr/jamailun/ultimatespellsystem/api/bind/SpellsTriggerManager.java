package fr.jamailun.ultimatespellsystem.api.bind;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Listen for triggers for {@link Player Players}.
 */
public interface SpellsTriggerManager {

  /**
   * Propagate an action to a cast sequence.
   * @param player the player doing the action.
   * @param action the action to propagate.
   * @return a non-null {@link ActionResult}.
   */
  @NotNull ActionResult action(@NotNull Player player, @NotNull ItemBindTrigger action);

  /**
   * An item has been switched, or the player left : remove the current session of the player.
   * @param player player to reset.
   */
  void reset(@NotNull Player player);

  /**
   * A result to a call to {@link #action(Player, ItemBindTrigger)}.
   */
  enum ActionResult {

    /**
     * The action finished a step. Thus, the provided spell should be cast. The action should be cancelled.
     */
    SPELL_CAST,

    /**
     * The step is valid, but no spell can yet be cast : further triggers required. The action should be cancelled.
     */
    STEP_VALID,

    /**
     * The trigger has nothing to do with the triggers. Action should be ignored.
     */
    IGNORED

  }

}
