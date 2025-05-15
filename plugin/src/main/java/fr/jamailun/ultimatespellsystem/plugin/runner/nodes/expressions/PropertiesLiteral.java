package fr.jamailun.ultimatespellsystem.plugin.runner.nodes.expressions;

import fr.jamailun.ultimatespellsystem.api.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.api.runner.SpellRuntime;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * A literal entries for a properties set.
 */
public class PropertiesLiteral extends RuntimeExpression {

    private final Map<String, RuntimeExpression> expressions;

    public PropertiesLiteral(Map<String, RuntimeExpression> expressions) {
        this.expressions = expressions;
    }

    @Override
    public Map<String, Object> evaluate(@NotNull SpellRuntime runtime) {
        Map<String, Object> map = new HashMap<>();
        for(String key : expressions.keySet()) {
            map.put(key, expressions.get(key).evaluate(runtime));
        }
        return map;
    }

    @Override
    public String toString() {
        return "{" + expressions + "}";
    }

    /**
     * Get a view on the properties keys.
     * @return a non-null, non-mutable collection of strings.
     */
    public @NotNull @UnmodifiableView Collection<String> keys() {
        return Collections.unmodifiableCollection(expressions.keySet());
    }
}
