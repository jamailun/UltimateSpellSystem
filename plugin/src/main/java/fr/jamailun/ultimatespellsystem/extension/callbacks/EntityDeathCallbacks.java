package fr.jamailun.ultimatespellsystem.extension.callbacks;

import fr.jamailun.ultimatespellsystem.api.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.api.entities.CallbackAction;
import fr.jamailun.ultimatespellsystem.api.entities.SummonAttributes;
import fr.jamailun.ultimatespellsystem.dsl.objects.CallbackEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

/**
 * Used by {@code LAND} callbacks.
 */
public class EntityDeathCallbacks extends CallbackProvider<EntityDeathEvent> {

    @EventHandler
    void onEvent(@NotNull EntityDeathEvent event) {
        SummonAttributes summon = UltimateSpellSystem.getSummonsManager().find(event.getEntity().getUniqueId());
        if(summon == null) return;
        summon.applyCallback(event);
    }

    @Override
    public @NotNull Collection<CallbackAction<EntityDeathEvent, ?>> getCallbacks() {
        return List.of(
                new CallbackAction<>(
                    CallbackEvent.of("die"),
                    EntityDeathEvent.class,
                    x -> null
                )
        );
    }

}
