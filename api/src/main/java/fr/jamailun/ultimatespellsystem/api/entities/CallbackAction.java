package fr.jamailun.ultimatespellsystem.api.entities;

import fr.jamailun.ultimatespellsystem.api.runner.RuntimeStatement;
import fr.jamailun.ultimatespellsystem.api.runner.SpellRuntime;
import fr.jamailun.ultimatespellsystem.dsl.objects.CallbackEvent;
import lombok.Getter;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

/**
 * An action for a callback.
 * @param <E> the generic event.
 * @param <A> the argument type.
 */
@Getter
public final class CallbackAction<E extends Event, A> {

    private final CallbackEvent dslDefinition;
    private final Class<E> listenedEvent;
    private final Function<E, A> argumentExtractor;

    /**
     * Create a new callback definition.
     * @param dslDefinition DSL definition to register.
     * @param listenedEvent type of event to react to.
     * @param argumentExtractor argument extractor. Must extract an argument from the event instance (matching the type).
     */
    public CallbackAction(@NotNull CallbackEvent dslDefinition, @NotNull Class<E> listenedEvent, @NotNull Function<E, A> argumentExtractor) {
        this.dslDefinition = dslDefinition;
        this.listenedEvent = listenedEvent;
        this.argumentExtractor = argumentExtractor;
    }

    /**
     * Register this callback to a summoned entity.
     * @param summon the summon attributes.
     * @param argVarName optional argument name. If null no argument value will be registered.
     * @param runtime spell runtime.
     * @param child statement to execute when the callback triggers.
     */
    public void registerToSummon(@NotNull SummonAttributes summon, String argVarName, @NotNull SpellRuntime runtime, @NotNull RuntimeStatement child) {
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
