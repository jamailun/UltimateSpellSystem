package fr.jamailun.ultimatespellsystem.bukkit.runner;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Map;

public abstract class RuntimeStatement {

    public abstract void run(@NotNull SpellRuntime runtime);

    @SuppressWarnings("unchecked")
    protected @NotNull Map<String, Object> getProperties(@Nullable RuntimeExpression optProperty, @NotNull SpellRuntime runtime) {
        if(optProperty != null) {
            // Already checked by type validation !
            return (Map<String, Object>) optProperty.evaluate(runtime);
        }
        return Collections.emptyMap();
    }

}
