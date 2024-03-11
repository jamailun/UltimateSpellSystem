package fr.jamailun.ultimatespellsystem.dsl.registries;

import fr.jamailun.ultimatespellsystem.dsl.nodes.ExpressionNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public final class CustomExpressionsRegistry {
    private CustomExpressionsRegistry() {}

    private final static Map<String, CustomExpressionProvider> REGISTRY = new HashMap<>();

    public static void register(String label, CustomExpressionProvider expressionFunction) {
        REGISTRY.put(label, expressionFunction);
    }

    public static @Nullable CustomExpressionProvider find(String label) {
        return REGISTRY.get(label);
    }

    public interface CustomExpressionProvider extends Function<List<ExpressionNode>, CustomExpression> { }

}
