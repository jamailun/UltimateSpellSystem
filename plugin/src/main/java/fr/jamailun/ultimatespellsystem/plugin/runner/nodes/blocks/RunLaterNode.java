package fr.jamailun.ultimatespellsystem.plugin.runner.nodes.blocks;

import fr.jamailun.ultimatespellsystem.api.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Duration;
import fr.jamailun.ultimatespellsystem.api.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.api.runner.RuntimeStatement;
import fr.jamailun.ultimatespellsystem.api.runner.SpellRuntime;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class RunLaterNode extends RuntimeStatement {

    private final RuntimeExpression duration;
    private final RuntimeStatement child;

    @Override
    public void run(@NotNull SpellRuntime runtime) {
        Duration duration = runtime.safeEvaluate(this.duration, Duration.class);
        UltimateSpellSystem.runTaskLater(
                () -> child.run(runtime),
                duration.toTicks()
        );
    }
}
