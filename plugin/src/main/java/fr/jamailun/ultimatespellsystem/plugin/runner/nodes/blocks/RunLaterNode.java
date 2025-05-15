package fr.jamailun.ultimatespellsystem.plugin.runner.nodes.blocks;

import fr.jamailun.ultimatespellsystem.UssLogger;
import fr.jamailun.ultimatespellsystem.api.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Duration;
import fr.jamailun.ultimatespellsystem.api.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.api.runner.RuntimeStatement;
import fr.jamailun.ultimatespellsystem.api.runner.SpellRuntime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class RunLaterNode extends RuntimeStatement {

    private final RuntimeExpression duration;
    @Getter private final RuntimeStatement child;

    @Override
    public void run(@NotNull SpellRuntime runtime) {
        Duration duration = runtime.safeEvaluate(this.duration, Duration.class);
        if(duration == null) {
            UssLogger.logWarning("RunLater:duration is null.");
            return;
        }
        UltimateSpellSystem.getScheduler().runTaskLater(
                () -> child.run(runtime),
                duration.toTicks()
        );
    }
}
