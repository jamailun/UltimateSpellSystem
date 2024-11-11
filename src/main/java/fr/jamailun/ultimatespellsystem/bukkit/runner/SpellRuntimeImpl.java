package fr.jamailun.ultimatespellsystem.bukkit.runner;

import fr.jamailun.ultimatespellsystem.api.bukkit.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.api.bukkit.runner.SpellRuntime;
import fr.jamailun.ultimatespellsystem.api.bukkit.runner.VariablesSet;
import fr.jamailun.ultimatespellsystem.bukkit.spells.BukkitSpellEntity;
import lombok.Getter;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of Spell Runtime.
 */
public final class SpellRuntimeImpl implements SpellRuntime {

    private final VariablesSet variables = new VariablesSetImpl();
    @Getter private final LivingEntity caster;
    private Integer exitCode;

    /**
     * Create a new context.
     * @param caster the caster to declare.
     */
    public SpellRuntimeImpl(@NotNull LivingEntity caster) {
        this.caster = caster;
        variables.set("caster", new BukkitSpellEntity(caster));
    }

    private SpellRuntimeImpl(@NotNull LivingEntity caster, VariablesSet variables, @Nullable Integer exitCode) {
        this.caster = caster;
        this.variables.copy(variables);
        this.exitCode = exitCode;
    }

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
            return clazz.cast(list.get(0));
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
    public @NotNull SpellRuntime makeChild() {
        return new SpellRuntimeImpl(caster, variables, exitCode);
    }

    @Override
    public int getFinalExitCode() {
        return exitCode == null ? 0 : exitCode;
    }

}
