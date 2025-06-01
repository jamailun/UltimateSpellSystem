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
import org.jetbrains.annotations.Nullable;

public class RunRepeatNode extends RuntimeStatement {

    private final RuntimeExpression period, optDelay;
    private final RuntimeExpression ofTotalCount, ofTotalDuration;
    @Getter private final RuntimeStatement child;

    public RunRepeatNode(@NotNull RuntimeExpression period, @NotNull RuntimeStatement child, @Nullable RuntimeExpression optDelay, @Nullable RuntimeExpression ofTotalCount, @Nullable RuntimeExpression ofTotalDuration) {
        this.period = period;
        this.child = child;
        this.optDelay = optDelay;
        this.ofTotalCount = ofTotalCount;
        this.ofTotalDuration = ofTotalDuration;
    }

    private int getCount(@NotNull SpellRuntime runtime, @NotNull Duration period) {
        // Freq + count
        if(ofTotalCount != null) {
            Double rawCount = runtime.safeEvaluate(ofTotalCount, Double.class);
            if(rawCount == null) {
                UssLogger.logWarning("RepeatNode:count is null.");
                return 0;
            }
            return rawCount.intValue();
        }

        // Freq + duration
        Duration duration = runtime.safeEvaluate(ofTotalDuration, Duration.class);
        if(duration == null) {
            UssLogger.logWarning("RepeatNode:duration is null.");
            return 0;
        }

        return (int) duration.div(period);
    }

    @Override
    public void run(@NotNull SpellRuntime runtime) {
        Duration period = runtime.safeEvaluate(this.period, Duration.class);
        if(period == null) {
            UssLogger.logWarning("RepeatNode:period is null.");
            return;
        }
        Duration delay = runtime.safeEvaluate(this.optDelay, Duration.class);
        int count = getCount(runtime, period);
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
