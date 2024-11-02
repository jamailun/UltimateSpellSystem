package fr.jamailun.ultimatespellsystem.bukkit.runner.nodes.expressions;

import fr.jamailun.ultimatespellsystem.bukkit.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.bukkit.runner.SpellRuntime;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class SizeOfNode extends RuntimeExpression {

    private final RuntimeExpression child;

    @Override
    public Integer evaluate(SpellRuntime runtime) {
        List<Object> list = runtime.safeEvaluateAcceptsList(child, Object.class);
        return list.size();
    }
}
