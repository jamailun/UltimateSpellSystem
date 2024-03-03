package fr.jamailun.ultimatespellsystem.bukkit.runner.nodes.blocks;

import fr.jamailun.ultimatespellsystem.bukkit.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.bukkit.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.bukkit.runner.RuntimeStatement;
import fr.jamailun.ultimatespellsystem.bukkit.runner.SpellRuntime;

public class WhileLoopNode extends RuntimeStatement {

    private final RuntimeExpression condition;
    private final RuntimeStatement child;
    private final boolean whileFirst;


    //TODO make the safeguard configurable !
    private final static int MAX_ITERATIONS = 4096;


    public WhileLoopNode(RuntimeExpression condition, RuntimeStatement child, boolean whileFirst) {
        this.condition = condition;
        this.child = child;
        this.whileFirst = whileFirst;
    }

    @Override
    public void run(SpellRuntime runtimeParent) {
        SpellRuntime runtime = runtimeParent.makeChild();

        RunInstance run = new RunInstance(runtime);

        if(whileFirst) {
            while(run.conditionValid()) {
                run.applyIteration();
                if(runtime.isStopped())
                    return;
            }
        } else {
            do {
                run.applyIteration();
                if(runtime.isStopped())
                    return;
            } while(run.conditionValid());
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
        }

        boolean conditionValid() {
            Object eval = condition.evaluate(runtime);
            if(eval instanceof Boolean bool) {
                if(iterationCount < MAX_ITERATIONS)
                    return bool;
                UltimateSpellSystem.logWarning("WhileLoop : forcefully existed after " + iterationCount + " iterations.");
                return false;
            }
            UltimateSpellSystem.logWarning("WhileLoop : unexpected type " + eval);
            return false;
        }

    }
}
