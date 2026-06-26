package fr.jamailun.ultimatespellsystem.extension.callbacks;

import fr.jamailun.ultimatespellsystem.api.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.api.entities.CallbackAction;
import fr.jamailun.ultimatespellsystem.api.entities.SummonAttributes;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.dsl.objects.CallbackEvent;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenType;
import fr.jamailun.ultimatespellsystem.plugin.entities.BukkitSpellEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

/**
 * Used by {@code LAND} callbacks.
 */
public class ProjectileLandCallbacks extends CallbackProvider<ProjectileHitEvent> {

  @EventHandler
  void onEvent(@NotNull ProjectileHitEvent event) {
    SummonAttributes summon = UltimateSpellSystem.getSummonsManager().find(event.getEntity().getUniqueId());
    if (summon != null)
      summon.applyCallback(event);
  }

  @Override
  public @NotNull Collection<CallbackAction<ProjectileHitEvent, ?>> getCallbacks() {
    return List.of(
        // Always valid.
        new CallbackAction<>(
            CallbackEvent.of("landed", TokenType.AT, TypePrimitive.LOCATION),
            ProjectileHitEvent.class,
            e -> e.getEntity().getLocation().clone()
        ),

        // Hit (requires an entity)
        new CallbackAction<>(
            CallbackEvent.of("hit", TokenType.TO, TypePrimitive.ENTITY),
            ProjectileHitEvent.class,
            e -> e.getHitEntity() == null ? null : new BukkitSpellEntity(e.getHitEntity()),
            e -> e.getHitEntity() != null // Must have an entity to be triggered
        )
    );
  }

}
