package fr.jamailun.ultimatespellsystem.bukkit.runner.nodes.expressions;

import fr.jamailun.ultimatespellsystem.bukkit.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.bukkit.runner.SpellRuntime;

public class NullLiteral extends RuntimeExpression {

    @Override
    public Void evaluate(SpellRuntime runtime) {
        return null;
    }

    @Override
    public String toString() {
        return "null";
    }
}
