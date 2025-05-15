package fr.jamailun.ultimatespellsystem.plugin.runner.nodes.expressions;

import fr.jamailun.ultimatespellsystem.api.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.api.runner.SpellRuntime;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.function.Consumer;

public class ArrayNode extends RuntimeExpression {

    private final List<RuntimeExpression> elements;

    public ArrayNode(List<RuntimeExpression> elements) {
        this.elements = elements;
    }

    @Override
    public List<Object> evaluate(@NotNull SpellRuntime runtime) {
        List<Object> list = new ArrayList<>();
        for(RuntimeExpression elem : elements) {
            list.add(elem.evaluate(runtime));
        }
        return list;
    }

    @Override
    public String toString() {
        StringJoiner sj = new StringJoiner(", ");
        elements.forEach(e -> sj.add(elements.toString()));
        return "[["+sj+"]]";
    }

    public void foreach(@NotNull Consumer<RuntimeExpression> function) {
        elements.forEach(function);
    }
}
