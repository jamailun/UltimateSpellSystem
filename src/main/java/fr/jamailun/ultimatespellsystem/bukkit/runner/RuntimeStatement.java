package fr.jamailun.ultimatespellsystem.bukkit.runner;

import java.util.Collections;
import java.util.Map;

public abstract class RuntimeStatement {

    public abstract void run(SpellRuntime runtime);

    @SuppressWarnings("unchecked")
    protected Map<String, Object> getProperties(RuntimeExpression optProperty, SpellRuntime runtime) {
        if(optProperty != null) {
            // Already checked by type validation !
            return (Map<String, Object>) optProperty.evaluate(runtime);
        }
        return Collections.emptyMap();
    }

}
