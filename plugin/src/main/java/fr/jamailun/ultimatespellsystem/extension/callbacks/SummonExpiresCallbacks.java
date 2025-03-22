package fr.jamailun.ultimatespellsystem.extension.callbacks;

import fr.jamailun.ultimatespellsystem.api.entities.CallbackAction;
import fr.jamailun.ultimatespellsystem.api.events.SummonedEntityExpiredEvent;
import fr.jamailun.ultimatespellsystem.dsl.objects.CallbackEvent;
import org.bukkit.event.EventHandler;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

/**
 * Used by {@code LAND} callbacks.
 */
public class SummonExpiresCallbacks extends CallbackProvider<SummonedEntityExpiredEvent> {

    @EventHandler
    void onEvent(@NotNull SummonedEntityExpiredEvent event) {
        event.getSummon().applyCallback(event);
    }

    @Override
    public @NotNull Collection<CallbackAction<SummonedEntityExpiredEvent, ?>> getCallbacks() {
        return List.of(
                new CallbackAction<>(
                    CallbackEvent.of("expire"),
                    SummonedEntityExpiredEvent.class,
                    x -> null
                )
        );
    }

}
