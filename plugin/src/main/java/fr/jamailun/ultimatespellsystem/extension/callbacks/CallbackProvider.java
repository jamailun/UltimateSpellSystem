package fr.jamailun.ultimatespellsystem.extension.callbacks;

import fr.jamailun.ultimatespellsystem.api.entities.CallbackAction;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public abstract class CallbackProvider<E extends Event> implements Listener {

    @Contract("-> new")
    public abstract @NotNull Collection<CallbackAction<E, ?>> getCallbacks();

}
