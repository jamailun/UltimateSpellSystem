package fr.jamailun.ultimatespellsystem.plugin.runner.nodes.blocks;

import fr.jamailun.ultimatespellsystem.UssLogger;
import fr.jamailun.ultimatespellsystem.api.runner.FlowState;
import fr.jamailun.ultimatespellsystem.api.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.api.runner.RuntimeStatement;
import fr.jamailun.ultimatespellsystem.api.runner.SpellRuntime;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ForLoopNode extends RuntimeStatement {

    private final RuntimeExpression condition;
    private final @Nullable RuntimeStatement initializer, iteration;
    @Getter private final RuntimeStatement child;

    //TODO make the safeguard configurable !
    private final static int MAX_ITERATIONS = 4096;

    public ForLoopNode(@Nullable RuntimeStatement initializer, @NotNull RuntimeExpression condition, @Nullable RuntimeStatement iteration, @NotNull RuntimeStatement child) {
        this.initializer = initializer;
        this.condition = condition;
        this.iteration = iteration;
        this.child = child;
    }

    @Override
    public void run(@NotNull SpellRuntime runtimeParent) {
        SpellRuntime runtime = runtimeParent.makeChild();

        if(initializer != null) initializer.run(runtime);
        RunInstance run = new RunInstance(runtime);

        while(run.conditionValid()) {
            run.applyIteration();

            // Flow management
            FlowState flow = runtime.getFlowState();
            if(flow.isNotRunning()) {
                if(flow == FlowState.BROKEN_CONTINUE)
                    runtime.acceptContinue(); // reset and continue
                else
                    return; // quit the loop
            }
        }
    }

    private class RunInstance {
        int iterationCount = 0;
        final SpellRuntime runtime;

        RunInstance(SpellRuntime runtime) {
            this.runtime = runtime;
        }

        void applyIteration() {
            iterationCount++;
            child.run(runtime);
            if(iteration != null) iteration.run(runtime);
        }

        boolean conditionValid() {
            Object eval = condition.evaluate(runtime);
            if(eval instanceof Boolean bool) {
                if(iterationCount < MAX_ITERATIONS)
                    return bool;
                UssLogger.logWarning("ForLoop : forcefully existed after " + iterationCount + " iterations.");
                return false;
            }
            UssLogger.logWarning("ForLoop : unexpected type " + eval);
            return false;
        }

    }
}
