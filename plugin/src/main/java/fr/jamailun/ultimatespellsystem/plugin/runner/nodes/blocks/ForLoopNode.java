package fr.jamailun.ultimatespellsystem.plugin.runner.nodes.blocks;

import fr.jamailun.ultimatespellsystem.api.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.api.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.api.runner.RuntimeStatement;
import fr.jamailun.ultimatespellsystem.api.runner.SpellRuntime;
import org.jetbrains.annotations.NotNull;

public class ForLoopNode extends RuntimeStatement {

    private final RuntimeExpression condition;
    private final RuntimeStatement initializer, iteration;
    private final RuntimeStatement child;


    //TODO make the safeguard configurable !
    private final static int MAX_ITERATIONS = 4096;


    public ForLoopNode( RuntimeStatement initializer, RuntimeExpression condition, RuntimeStatement iteration, RuntimeStatement child) {
        this.initializer = initializer;
        this.condition = condition;
        this.iteration = iteration;
        this.child = child;
    }

    @Override
    public void run(@NotNull SpellRuntime runtimeParent) {
        SpellRuntime runtime = runtimeParent.makeChild();

        initializer.run(runtime);
        RunInstance run = new RunInstance(runtime);

        while(run.conditionValid()) {
            run.applyIteration();
            if(runtime.getFlowState().isNotRunning())
                return;
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
            iteration.run(runtime);
        }

        boolean conditionValid() {
            Object eval = condition.evaluate(runtime);
            if(eval instanceof Boolean bool) {
                if(iterationCount < MAX_ITERATIONS)
                    return bool;
                UltimateSpellSystem.logWarning("ForLoop : forcefully existed after " + iterationCount + " iterations.");
                return false;
            }
            UltimateSpellSystem.logWarning("ForLoop : unexpected type " + eval);
            return false;
        }

    }
}
