package fr.jamailun.ultimatespellsystem.plugin.runner.nodes.expressions;

import fr.jamailun.ultimatespellsystem.api.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.api.runner.SpellRuntime;
import org.jetbrains.annotations.NotNull;

public class NullLiteral extends RuntimeExpression {

    @Override
    public Void evaluate(@NotNull SpellRuntime runtime) {
        return null;
    }

    @Override
    public String toString() {
        return "null";
    }
}
