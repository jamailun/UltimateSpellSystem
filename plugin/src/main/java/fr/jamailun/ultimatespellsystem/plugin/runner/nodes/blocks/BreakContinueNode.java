package fr.jamailun.ultimatespellsystem.plugin.runner.nodes.blocks;

import fr.jamailun.ultimatespellsystem.api.runner.RuntimeStatement;
import fr.jamailun.ultimatespellsystem.api.runner.SpellRuntime;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class BreakContinueNode extends RuntimeStatement {

    private final boolean isContinue;

    @Override
    public void run(@NotNull SpellRuntime runtime) {
        if(isContinue) {
            runtime.statementContinue();
        } else {
            runtime.statementBreak();
        }
    }
}
