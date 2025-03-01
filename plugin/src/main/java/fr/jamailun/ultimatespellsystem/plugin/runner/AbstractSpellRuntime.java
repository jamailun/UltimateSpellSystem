package fr.jamailun.ultimatespellsystem.plugin.runner;

import fr.jamailun.ultimatespellsystem.api.runner.FlowState;
import fr.jamailun.ultimatespellsystem.api.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.api.runner.SpellRuntime;
import fr.jamailun.ultimatespellsystem.api.runner.VariablesSet;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Generic implementation of a {@link SpellRuntime}.
 */
public abstract class AbstractSpellRuntime implements SpellRuntime {

    protected final VariablesSet variables;
    protected final ExitCode exitCode;

    protected boolean flagBreak = false;
    protected boolean flagContinue = false;

    AbstractSpellRuntime(@NotNull AbstractSpellRuntime parent) {
        exitCode = parent.exitCode;
        variables = parent.variables.inherit();
        flagContinue = parent.flagContinue;
        flagBreak = parent.flagBreak;
    }

    AbstractSpellRuntime(@NotNull ExitCode exitCode) {
        this.exitCode = exitCode;
        variables = new VariablesSetImpl();
    }

    @Override
    public boolean isStopped() {
        return exitCode.isSet();
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
        this.exitCode.set(exitCode);
    }

    @Override
    public int getFinalExitCode() {
        return exitCode.getCode();
    }

    @Getter
    public static class ExitCode {
        private int code = 0;
        private boolean set = false;
        public void set(int value) {
            if(!set) {
                set = true;
                code = value;
            }
        }
    }

    @Override
    public void statementBreak() {
        flagBreak = true;
    }

    @Override
    public void statementContinue() {
        flagBreak = true;
        flagContinue = true;
    }

    @Override
    public @NotNull FlowState getFlowState() {
        if(isStopped()) return FlowState.STOPPED;
        if(flagContinue) return FlowState.BROKEN_CONTINUE;
        if(flagBreak) return FlowState.BROKEN;
        return FlowState.RUNNING;
    }

}
