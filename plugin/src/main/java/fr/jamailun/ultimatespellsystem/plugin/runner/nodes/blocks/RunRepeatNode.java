package fr.jamailun.ultimatespellsystem.plugin.runner.nodes.blocks;

import fr.jamailun.ultimatespellsystem.UssLogger;
import fr.jamailun.ultimatespellsystem.api.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.dsl.nodes.statements.blocks.RepeatStatement;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Duration;
import fr.jamailun.ultimatespellsystem.api.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.api.runner.RuntimeStatement;
import fr.jamailun.ultimatespellsystem.api.runner.SpellRuntime;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public class RunRepeatNode extends RuntimeStatement {

    private final RuntimeExpression period, optDelay;
    private final RuntimeExpression count;
    @Getter private final RuntimeStatement child;

    public RunRepeatNode(RuntimeExpression period, RuntimeStatement child, RuntimeExpression optDelay, RuntimeExpression count) {
        this.period = period;
        this.child = child;
        this.optDelay = optDelay;
        this.count = count;
    }

    @Override
    public void run(@NotNull SpellRuntime runtime) {
        Duration period = runtime.safeEvaluate(this.period, Duration.class);
        Duration delay = runtime.safeEvaluate(this.optDelay, Duration.class);
        Double rawCount = runtime.safeEvaluate(this.count, Double.class);
        if(rawCount == null) {
            UssLogger.logWarning("RepeatNode:count is null.");
            return;
        }
        if(period == null) {
            UssLogger.logWarning("RepeatNode:period is null.");
            return;
        }
        int count = rawCount.intValue();

        long delayTicks = delay == null ? 0 : delay.toTicks();

        UltimateSpellSystem.getScheduler().runTaskRepeat(
                new Runnable() {
                    private int count = 0;
                    @Override
                    public void run() {
                        runtime.variables().set(RepeatStatement.INDEX_VARIABLE, count);
                        try {
                            child.run(runtime);
                        } catch (Exception t) {
                            UssLogger.logError("Uncaught "+t.getClass().getSimpleName()+" on RunRepeatNode#run", t);
                        }
                        count++;
                    }
                },
                count,
                delayTicks,
                period.toTicks()
        );
    }
}
