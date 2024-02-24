package fr.jamailun.ultimatespellsystem.runner.nodes.blocks;

import fr.jamailun.ultimatespellsystem.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Duration;
import fr.jamailun.ultimatespellsystem.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.runner.RuntimeStatement;
import fr.jamailun.ultimatespellsystem.runner.SpellRuntime;

public class RunLaterNode extends RuntimeStatement {

    private final RuntimeExpression duration;
    private final RuntimeStatement child;

    public RunLaterNode(RuntimeExpression duration, RuntimeStatement child) {
        this.duration = duration;
        this.child = child;
    }

    @Override
    public void run(SpellRuntime runtime) {
        Duration duration = runtime.safeEvaluate(this.duration, Duration.class);
        UltimateSpellSystem.runTaskLater(
                () -> child.run(runtime),
                duration.toTicks()
        );
    }
}
