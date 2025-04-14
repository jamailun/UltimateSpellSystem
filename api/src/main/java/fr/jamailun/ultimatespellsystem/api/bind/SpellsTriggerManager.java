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
   * @return {@code true} if the event should be cancelled.
   */
  boolean action(@NotNull Player player, @NotNull ItemBindTrigger action);

  /**
   * An item has been switched, or the player left : remove the current session of the player.
   * @param player player to reset.
   */
  void reset(@NotNull Player player);

}
