package fr.jamailun.ultimatespellsystem.plugin.runner;

import fr.jamailun.ultimatespellsystem.api.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.api.runner.SpellRuntime;
import fr.jamailun.ultimatespellsystem.api.runner.VariablesSet;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Generic implementation of a {@link SpellRuntime}.
 */
public abstract class AbstractSpellRuntime implements SpellRuntime {

    protected final VariablesSet variables = new VariablesSetImpl();
    protected Integer exitCode;

    @Override
    public boolean isStopped() {
        return exitCode != null;
    }

    @Override
    public @NotNull VariablesSet variables() {
        return variables;
    }

    @Override
    public <T> T safeEvaluate(RuntimeExpression expression, Class<T> clazz) {
        if(expression == null)
            return null;
        Object value = expression.evaluate(this);
        if(value instanceof List<?> list && list.size() == 1) {
            return clazz.cast(list.getFirst());
        }
        return clazz.cast(value);
    }

    @Override
    public <T> List<T> safeEvaluateList(RuntimeExpression expression, Class<T> clazz) {
        if(expression == null)
            return null;
        Object value = expression.evaluate(this);
        if(value instanceof Collection<?> c) {
            return c.stream().map(clazz::cast).collect(Collectors.toCollection(ArrayList::new));
        }
        return null;
    }

    @Override
    public <T> List<T> safeEvaluateAcceptsList(RuntimeExpression expression, Class<T> clazz) {
        if(expression == null)
            return Collections.emptyList();
        Object value = expression.evaluate(this);
        if(value instanceof Collection<?> c) {
            return c.stream().map(clazz::cast).collect(Collectors.toCollection(ArrayList::new));
        }
        T singleton = clazz.cast(value);
        return Collections.singletonList(singleton);
    }

    @Override
    public void stop(int exitCode) {
        this.exitCode = exitCode;
    }

    @Override
    public int getFinalExitCode() {
        return exitCode == null ? 0 : exitCode;
    }

}
