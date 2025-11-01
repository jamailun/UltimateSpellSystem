package fr.jamailun.ultimatespellsystem.plugin.runner;

import fr.jamailun.ultimatespellsystem.api.runner.FlowState;
import fr.jamailun.ultimatespellsystem.api.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.api.runner.SpellRuntime;
import fr.jamailun.ultimatespellsystem.api.runner.VariablesSet;
import fr.jamailun.ultimatespellsystem.api.runner.structs.Struct;
import fr.jamailun.ultimatespellsystem.api.spells.Spell;
import fr.jamailun.ultimatespellsystem.plugin.runner.structs.StructsLibrary;
import lombok.Getter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Generic implementation of a {@link SpellRuntime}.
 */
public abstract class AbstractSpellRuntime implements SpellRuntime {

    protected final Map<StructPtr, Struct> cachedStructures = new HashMap<>();
    protected final StructsLibrary structsLibrary;
    protected final VariablesSet variables;
    protected final ExitCode exitCode;
    @Getter protected final Spell spell;

    protected boolean flagBreak = false;
    protected boolean flagContinue = false;
    protected final boolean inFunction;

    AbstractSpellRuntime(@NotNull AbstractSpellRuntime parent, boolean inFunction) {
        exitCode = parent.exitCode;
        variables = parent.variables.inherit();
        flagContinue = parent.flagContinue;
        flagBreak = parent.flagBreak;
        spell = parent.spell;
        cachedStructures.putAll(parent.cachedStructures);
        this.inFunction = inFunction;
        structsLibrary = parent.structsLibrary;
    }

    AbstractSpellRuntime(@NotNull ExitCode exitCode, @Nullable Spell spell) {
        this.exitCode = exitCode;
        variables = new VariablesSetImpl();
        this.spell = spell;
        this.structsLibrary = new StructsLibrary();
        inFunction = false;
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
    public <T> @Nullable T safeEvaluate(RuntimeExpression expression, Class<T> clazz) {
        if(expression == null)
            return null;
        Object value = expression.evaluate(this);
        if(value instanceof List<?> list && list.size() == 1) {
            return clazz.cast(list.getFirst());
        }
        return clazz.cast(value);
    }

    @Override
    public <T> @NotNull List<T> safeEvaluateList(RuntimeExpression expression, Class<T> clazz) {
        if(expression == null)
            return Collections.emptyList();
        Object value = expression.evaluate(this);
        if(value instanceof Collection<?> c) {
            return c.stream().map(clazz::cast).collect(Collectors.toCollection(ArrayList::new));
        }
        return Collections.emptyList();
    }

    @Override
    public <T> @NotNull List<T> safeEvaluateAcceptsList(RuntimeExpression expression, Class<T> clazz) {
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
    public @Nullable Object getReturnedValue() {
        return exitCode.getValue();
    }

    @Override
    public void statementReturn(@Nullable Object value) {
        exitCode.set(value);
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
    public void acceptContinue() {
        if(flagContinue) {
            flagBreak = false;
            flagContinue = false;
        }
    }

    @Override
    public @NotNull FlowState getFlowState() {
        if(isStopped()) return FlowState.STOPPED;
        if(flagContinue) return FlowState.BROKEN_CONTINUE;
        if(flagBreak) return FlowState.BROKEN;
        return FlowState.RUNNING;
    }

    @Override
    public @Nullable Struct getStructOf(@NotNull String structName, @Nullable Object value) {
        if(value == null) return null;

        StructPtr ptr = StructPtr.of(structName, value);
        return cachedStructures.computeIfAbsent(ptr, x ->
                structsLibrary.instantiate(structName, value)
        );
    }

    // ---- utils

    @Getter
    public static class ExitCode {
        private Object value;
        private boolean set = false;
        public void set(Object value) {
            if(!set) {
                set = true;
                this.value = value;
            }
        }
    }

    protected record StructPtr(
       String structName,
       int objHash
    ) {
        @Contract("_, _ -> new")
        static @NotNull StructPtr of(String structName, Object object) {
            return new StructPtr(structName, Objects.hashCode(object));
        }
    }
}
