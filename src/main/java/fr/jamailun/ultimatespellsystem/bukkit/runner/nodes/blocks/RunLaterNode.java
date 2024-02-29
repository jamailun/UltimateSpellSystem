package fr.jamailun.ultimatespellsystem.bukkit.runner.nodes.blocks;

import fr.jamailun.ultimatespellsystem.bukkit.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Duration;
import fr.jamailun.ultimatespellsystem.bukkit.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.bukkit.runner.RuntimeStatement;
import fr.jamailun.ultimatespellsystem.bukkit.runner.SpellRuntime;

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
