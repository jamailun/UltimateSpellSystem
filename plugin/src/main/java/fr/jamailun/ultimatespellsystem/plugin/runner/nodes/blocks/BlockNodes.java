package fr.jamailun.ultimatespellsystem.plugin.runner.nodes.blocks;

import fr.jamailun.ultimatespellsystem.api.runner.FlowState;
import fr.jamailun.ultimatespellsystem.api.runner.RuntimeStatement;
import fr.jamailun.ultimatespellsystem.api.runner.SpellRuntime;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.StringJoiner;

public class BlockNodes extends RuntimeStatement {

    private final List<RuntimeStatement> children;

    public BlockNodes(List<RuntimeStatement> children) {
        this.children = children;
    }

    @Override
    public void run(@NotNull SpellRuntime runtime) {
        SpellRuntime childRuntime = runtime.makeChild();
        for(RuntimeStatement child : children) {
            child.run(childRuntime);
            FlowState flow = childRuntime.getFlowState();

            if(flow.isNotRunning()) {
                if(flow == FlowState.BROKEN) {
                    runtime.statementBreak(); // propagate to parent !
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
