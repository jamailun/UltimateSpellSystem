package fr.jamailun.ultimatespellsystem.bukkit.runner.nodes.expressions;

import fr.jamailun.ultimatespellsystem.api.bukkit.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.api.bukkit.runner.SpellRuntime;
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
