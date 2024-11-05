package fr.jamailun.ultimatespellsystem.bukkit.runner.nodes.expressions;

import fr.jamailun.ultimatespellsystem.bukkit.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.bukkit.runner.SpellRuntime;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

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
}
