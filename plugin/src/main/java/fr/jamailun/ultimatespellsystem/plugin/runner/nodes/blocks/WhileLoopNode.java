package fr.jamailun.ultimatespellsystem.plugin.runner.nodes.blocks;

import fr.jamailun.ultimatespellsystem.UssLogger;
import fr.jamailun.ultimatespellsystem.api.runner.FlowState;
import fr.jamailun.ultimatespellsystem.api.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.api.runner.RuntimeStatement;
import fr.jamailun.ultimatespellsystem.api.runner.SpellRuntime;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class WhileLoopNode extends RuntimeStatement {

    //TODO make the safeguard configurable !
    private final static int MAX_ITERATIONS = 4096;

    private final RuntimeExpression condition;
    private final RuntimeStatement child;
    private final boolean whileFirst;

    @Override
    public void run(@NotNull SpellRuntime runtimeParent) {
        SpellRuntime runtime = runtimeParent.makeChild();

        RunInstance run = new RunInstance(runtime);

        if(whileFirst) {
            while(run.conditionValid()) {
                if(iterate(run, runtime))
                    return;
            }
        } else {
            do {
                if(iterate(run, runtime))
                    return;
            } while(run.conditionValid());
        }

    }

    private boolean iterate(@NotNull RunInstance run, @NotNull SpellRuntime runtime) {
        run.applyIteration();
        // Flow management
        FlowState flow = runtime.getFlowState();
        if(flow.isNotRunning()) {
            if(flow == FlowState.BROKEN_CONTINUE)
                runtime.statementContinue();
            else
                return true;
        }
        return false;
    }

    /**
     * Internal run representation.
     */
    private class RunInstance {
        int iterationCount = 0;
        final SpellRuntime runtime;

        RunInstance(SpellRuntime runtime) {
            this.runtime = runtime;
        }

        void applyIteration() {
            iterationCount++;
            child.run(runtime);
        }

        boolean conditionValid() {
            Object eval = condition.evaluate(runtime);
            if(eval instanceof Boolean bool) {
                if(iterationCount < MAX_ITERATIONS)
                    return bool;
                UssLogger.logWarning("WhileLoop : forcefully existed after " + iterationCount + " iterations.");
                return false;
            }
            UssLogger.logWarning("WhileLoop : unexpected type " + eval);
            return false;
        }

    }
}
