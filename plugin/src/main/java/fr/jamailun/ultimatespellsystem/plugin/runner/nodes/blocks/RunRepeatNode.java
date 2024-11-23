package fr.jamailun.ultimatespellsystem.plugin.runner.nodes.blocks;

import fr.jamailun.ultimatespellsystem.api.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Duration;
import fr.jamailun.ultimatespellsystem.api.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.api.runner.RuntimeStatement;
import fr.jamailun.ultimatespellsystem.api.runner.SpellRuntime;
import org.jetbrains.annotations.NotNull;

public class RunRepeatNode extends RuntimeStatement {

    private final RuntimeExpression period, optDelay;
    private final RuntimeExpression count;
    private final RuntimeStatement child;

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
        int count = runtime.safeEvaluate(this.count, Double.class).intValue();

        long delayTicks = delay == null ? 0 : delay.toTicks();

        UltimateSpellSystem.runTaskRepeat(
                () -> {
                    try {
                        child.run(runtime);
                    } catch (Throwable t) {
                        UltimateSpellSystem.logError("Uncaught "+t.getClass().getSimpleName()+" on RunRepeatNode#run : " + t.getMessage());
                        t.printStackTrace();
                    }
                },
                count,
                delayTicks,
                period.toTicks()
        );
    }
}
