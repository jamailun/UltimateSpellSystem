package fr.jamailun.ultimatespellsystem.bukkit.runner.nodes.expressions;

import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.litteral.LiteralExpression;
import fr.jamailun.ultimatespellsystem.bukkit.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.bukkit.runner.SpellRuntime;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * A literal value.
 * @param <T> the type of the value.
 */
public class RawLiteral<T> extends RuntimeExpression {

    private final T raw;

    public RawLiteral(@NotNull LiteralExpression<T> expression) {
        this.raw = expression.getRaw();
    }
    public RawLiteral(T value) {
        this.raw = value;
    }

    @Override
    public T evaluate(@NotNull SpellRuntime runtime) {
        return raw;
    }

    @Override
    public String toString() {
        return Objects.toString(raw);
    }
}
