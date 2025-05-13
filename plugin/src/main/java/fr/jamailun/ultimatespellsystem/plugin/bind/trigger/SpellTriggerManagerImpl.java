package fr.jamailun.ultimatespellsystem.plugin.bind.trigger;

import fr.jamailun.ultimatespellsystem.api.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.api.bind.ItemBindTrigger;
import fr.jamailun.ultimatespellsystem.api.bind.SpellBindData;
import fr.jamailun.ultimatespellsystem.api.bind.SpellsTriggerManager;
import fr.jamailun.ultimatespellsystem.api.entities.SpellEntity;
import fr.jamailun.ultimatespellsystem.api.events.BoundSpellCastEvent;
import fr.jamailun.ultimatespellsystem.plugin.entities.BukkitSpellEntity;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Manages players action, and start triggers.
 */
public final class SpellTriggerManagerImpl implements SpellsTriggerManager {

  private final Map<UUID, SpellTriggerSession> sessions = new HashMap<>();

  @Override
  public @NotNull ActionResult action(@NotNull Player player, @NotNull ItemBindTrigger action) {
    int cd = player.getCooldown(player.getInventory().getItemInMainHand().getType());
    if(cd > 0 && player.getGameMode() != GameMode.CREATIVE)
      return ActionResult.IGNORED; // Still in cooldown :(

    UUID uuid = player.getUniqueId();
    if( ! sessions.containsKey(uuid) || sessions.get(uuid).isTooOldOrInvalid()) {
      ItemStack item = player.getInventory().getItemInMainHand();
      var data = UltimateSpellSystem.getItemBinder().getBindDatas(item);
      if(data.isEmpty()) { // No bound spell... Ignore the action.
        return ActionResult.IGNORED;
      }
      sessions.put(uuid, new SpellTriggerSession(player, item, data.get()));
    }

    SpellTriggerSession session = sessions.get(uuid);
    ActionRes res = session.action(action);
    if(res.data() != null) {
      cast(player, res.data(), session);
      sessions.remove(uuid);
    }
    return res.result();
  }

  @Override
  public void reset(@NotNull Player player) {
    sessions.remove(player.getUniqueId());
  }

  private void cast(@NotNull Player player, @NotNull SpellBindData data, @NotNull SpellTriggerSession session) {
    SpellEntity caster = new BukkitSpellEntity(player);
    boolean bypass = player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR;

    // can afford ?
    if(!bypass && !data.getCost().canPay(caster)) {
      player.sendMessage("§cYou cannot afford this spell.");
      return;
    }

    // Send event
    BoundSpellCastEvent cast = new BoundSpellCastEvent(player, data, session.getItem(), session.getLastAction());
    Bukkit.getPluginManager().callEvent(cast);

    // If cancelled, do nothing
    if(cast.isCancelled())
      return;

    // Not cancellable after that !
    boolean success = data.getSpell().castNotCancellable(caster);

    // Pay
    if(success && !bypass) {
      data.getCost().pay(caster);
    }
  }

}
