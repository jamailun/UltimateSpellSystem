package fr.jamailun.ultimatespellsystem.plugin.runner.nodes.expressions;

import fr.jamailun.ultimatespellsystem.api.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.api.runner.SpellRuntime;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@RequiredArgsConstructor
public class SizeOfNode extends RuntimeExpression {

    private final RuntimeExpression child;

    @Override
    public Integer evaluate(@NotNull SpellRuntime runtime) {
        List<Object> list = runtime.safeEvaluateAcceptsList(child, Object.class);
        return list.size();
    }
}
