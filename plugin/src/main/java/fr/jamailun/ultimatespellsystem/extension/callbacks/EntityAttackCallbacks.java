package fr.jamailun.ultimatespellsystem.extension.callbacks;

import fr.jamailun.ultimatespellsystem.api.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.api.entities.CallbackAction;
import fr.jamailun.ultimatespellsystem.api.entities.SummonAttributes;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.dsl.objects.CallbackEvent;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

/**
 * Used by {@code ATTACK} callbacks.
 */
public class EntityAttackCallbacks extends CallbackProvider<EntityDamageByEntityEvent> {

  @EventHandler
  void onEvent(@NotNull EntityDamageByEntityEvent event) {
    SummonAttributes summon = UltimateSpellSystem.getSummonsManager().find(event.getDamager().getUniqueId());
    if (summon != null)
      summon.applyCallback(event);
  }

  @Override
  public @NotNull Collection<CallbackAction<EntityDamageByEntityEvent, ?>> getCallbacks() {
    return List.of(
        new CallbackAction<>(
            CallbackEvent.of("attack", TokenType.AT, TypePrimitive.ENTITY),
            EntityDamageByEntityEvent.class,
            EntityEvent::getEntity
        )
    );
  }

}
