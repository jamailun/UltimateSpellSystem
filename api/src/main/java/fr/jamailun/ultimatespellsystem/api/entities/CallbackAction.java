package fr.jamailun.ultimatespellsystem.api.entities;

import fr.jamailun.ultimatespellsystem.api.runner.RuntimeStatement;
import fr.jamailun.ultimatespellsystem.api.runner.SpellRuntime;
import fr.jamailun.ultimatespellsystem.dsl.objects.CallbackEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

/**
 * An action for a callback.
 * @param <E> the generic event.
 * @param <A> the argument type.
 */
@RequiredArgsConstructor
@Getter
public final class CallbackAction<E extends Event, A> {

    private final CallbackEvent dslDefinition;
    private final Class<E> listenedEvent;
    private final Function<E, A> argumentExtractor;

    public void registerToSummon(@NotNull SummonAttributes summon, String argVarName, SpellRuntime runtime, RuntimeStatement child) {
        summon.registerCallback(listenedEvent, (event) -> {
            // 1. Register variable
            if(argVarName != null) {
                runtime.variables().set(argVarName, argumentExtractor.apply(event));
            }
            // 2. Execute child
            child.run(runtime);
        });
    }

}
