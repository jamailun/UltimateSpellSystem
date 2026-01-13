package fr.jamailun.ultimatespellsystem.plugin.runner.nodes.statements;

import fr.jamailun.ultimatespellsystem.api.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.api.runner.RuntimeStatement;
import fr.jamailun.ultimatespellsystem.api.runner.SpellRuntime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
@RequiredArgsConstructor
public class AffectVarNode extends RuntimeStatement {

    private final @NotNull RuntimeExpression holder;
    private final @Nullable RuntimeExpression expression;

    @Override
    public void run(@NotNull SpellRuntime runtime) {
        //TODO
    }
}
