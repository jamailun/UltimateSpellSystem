package fr.jamailun.ultimatespellsystem.plugin.runner.nodes.blocks;

import fr.jamailun.ultimatespellsystem.api.runner.FlowState;
import fr.jamailun.ultimatespellsystem.api.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.api.runner.RuntimeStatement;
import fr.jamailun.ultimatespellsystem.api.runner.SpellRuntime;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@RequiredArgsConstructor
public class ForeachLoopNode extends RuntimeStatement {

    private final String varName;
    private final RuntimeExpression source;
    private final RuntimeStatement child;

    @Override
    public void run(@NotNull SpellRuntime runtimeParent) {
        SpellRuntime runtime = runtimeParent.makeChild();

        List<Object> list = runtime.safeEvaluateList(source, Object.class);

        for(Object object : list) {
            runtime.variables().set(varName, object);
            child.run(runtime);

            // Flow management
            FlowState flow = runtime.getFlowState();
            if(flow.isNotRunning()) {
                if(flow == FlowState.BROKEN_CONTINUE)
                    runtime.statementContinue();
                else
                    return;
            }
        }
    }

}
