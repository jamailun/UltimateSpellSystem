package fr.jamailun.ultimatespellsystem.plugin.runner.nodes.blocks;

import fr.jamailun.ultimatespellsystem.api.runner.FlowState;
import fr.jamailun.ultimatespellsystem.api.runner.RuntimeStatement;
import fr.jamailun.ultimatespellsystem.api.runner.SpellRuntime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.StringJoiner;

@RequiredArgsConstructor
@Getter
public class BlockNodes extends RuntimeStatement {

    private final List<RuntimeStatement> children;

    @Override
    public void run(@NotNull SpellRuntime runtime) {
        SpellRuntime childRuntime = runtime.makeChild();
        for(RuntimeStatement child : children) {
            child.run(childRuntime);
            FlowState flow = childRuntime.getFlowState();

            if(flow.isNotRunning()) {
                // Propagate break to parent.
                if(flow == FlowState.BROKEN) {
                    runtime.statementBreak();
                }else if(flow == FlowState.BROKEN_CONTINUE) {
                    runtime.statementContinue();
                }
                return;
            }

        }
    }

    @Override
    public String toString() {
        StringJoiner sj = new StringJoiner(";\n", "{", "}");
        children.forEach(c -> sj.add(c.toString()));
        return sj.toString();
    }
}
