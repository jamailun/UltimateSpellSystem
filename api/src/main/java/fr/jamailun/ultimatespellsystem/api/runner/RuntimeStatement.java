package fr.jamailun.ultimatespellsystem.api.runner;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Map;

/**
 * A runtime-version of a statement.
 */
public abstract class RuntimeStatement {

    /**
     * Run the statement with a specific context.
     * @param runtime the non-null context to run this statement on.
     */
    public abstract void run(@NotNull SpellRuntime runtime);

    /**
     * Find a properties-set inside the runtime.
     * @param optProperty the nullable properties-set to search for.
     * @param runtime the runtime context to look at.
     * @return a new empty map is the first parameter is null.
     */
    @SuppressWarnings("unchecked")
    protected @NotNull Map<String, Object> getProperties(@Nullable RuntimeExpression optProperty, @NotNull SpellRuntime runtime) {
        if(optProperty != null) {
            // Already checked by type validation !
            return (Map<String, Object>) optProperty.evaluate(runtime);
        }
        return Collections.emptyMap();
    }

}
