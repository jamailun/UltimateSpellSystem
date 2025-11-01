package fr.jamailun.ultimatespellsystem.plugin.runner.nodes.statements;

import fr.jamailun.ultimatespellsystem.api.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.api.runner.RuntimeStatement;
import fr.jamailun.ultimatespellsystem.api.runner.SpellRuntime;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.type.Type;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
@RequiredArgsConstructor
public class DeclareVarNode extends RuntimeStatement {

    private final @NotNull String varName;
    private final @NotNull Type type;
    private final @Nullable RuntimeExpression expression;

    @Override
    public void run(@NotNull SpellRuntime runtime) {
        Object value = expression == null ? null : expression.evaluate(runtime);
        if(value != null) {
            //TODO match type ?!
        }
        runtime.variables().set(varName, value);
    }
}
