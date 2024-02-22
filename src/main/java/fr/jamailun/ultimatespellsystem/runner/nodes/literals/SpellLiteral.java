package fr.jamailun.ultimatespellsystem.runner.nodes.literals;

import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.litteral.LiteralExpression;
import fr.jamailun.ultimatespellsystem.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.runner.SpellRuntime;

public class SpellLiteral<T> extends RuntimeExpression {

    private final T raw;

    public SpellLiteral(LiteralExpression<T> expression) {
        this.raw = expression.getRaw();
    }

    @Override
    public Object evaluate(SpellRuntime runtime) {
        return raw;
    }
}
