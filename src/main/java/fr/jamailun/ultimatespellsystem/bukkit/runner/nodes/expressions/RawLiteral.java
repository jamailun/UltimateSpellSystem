package fr.jamailun.ultimatespellsystem.bukkit.runner.nodes.expressions;

import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.litteral.LiteralExpression;
import fr.jamailun.ultimatespellsystem.bukkit.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.bukkit.runner.SpellRuntime;

public class RawLiteral<T> extends RuntimeExpression {

    private final T raw;

    public RawLiteral(LiteralExpression<T> expression) {
        this.raw = expression.getRaw();
    }

    @Override
    public T evaluate(SpellRuntime runtime) {
        return raw;
    }
}
