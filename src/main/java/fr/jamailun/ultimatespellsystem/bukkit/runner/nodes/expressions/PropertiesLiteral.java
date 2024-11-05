package fr.jamailun.ultimatespellsystem.bukkit.runner.nodes.expressions;

import fr.jamailun.ultimatespellsystem.bukkit.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.bukkit.runner.SpellRuntime;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

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
}
