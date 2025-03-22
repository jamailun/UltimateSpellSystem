package fr.jamailun.ultimatespellsystem.extension.callbacks;

import fr.jamailun.ultimatespellsystem.api.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.api.entities.CallbackAction;
import fr.jamailun.ultimatespellsystem.api.entities.SummonAttributes;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.dsl.objects.CallbackEvent;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenType;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Used by {@code LAND} callbacks.
 */
public class ProjectileLandListener extends CallbackProvider<ProjectileHitEvent> {

    @EventHandler
    void onProjectileLand(@NotNull ProjectileHitEvent event) {
        if(event.getHitBlock() == null) return;
        SummonAttributes summon = UltimateSpellSystem.getSummonsManager().find(event.getEntity().getUniqueId());
        if(summon == null) return;
        summon.applyCallback(event);
    }

    @Override
    public @NotNull CallbackAction<ProjectileHitEvent, Location> getCallback() {
        return new CallbackAction<>(
                CallbackEvent.of("landed", TokenType.AT, TypePrimitive.LOCATION),
                ProjectileHitEvent.class,
                e -> e.getEntity().getLocation()
        );
    }

}
