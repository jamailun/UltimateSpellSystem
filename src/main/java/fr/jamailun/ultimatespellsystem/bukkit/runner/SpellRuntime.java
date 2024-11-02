package fr.jamailun.ultimatespellsystem.bukkit.runner;

import fr.jamailun.ultimatespellsystem.bukkit.spells.BukkitSpellEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SpellRuntime {

    private final VariablesSet variables = new VariablesSet();
    private final LivingEntity caster;
    private boolean stopped = false;

    public SpellRuntime(@NotNull LivingEntity caster) {
        this.caster = caster;
        variables.set("caster", new BukkitSpellEntity(caster));
    }

    private SpellRuntime(@NotNull LivingEntity caster, VariablesSet variables, boolean stopped) {
        this.caster = caster;
        this.variables.copy(variables);
        this.stopped = stopped;
    }

    public LivingEntity getCaster() {
        return caster;
    }

    public VariablesSet variables() {
        return variables;
    }

    public <T> T safeEvaluate(RuntimeExpression expression, Class<T> clazz) {
        if(expression == null)
            return null;
        Object value = expression.evaluate(this);
        if(value instanceof List<?> list && list.size() == 1) {
            return clazz.cast(list.get(0));
        }
        return clazz.cast(value);
    }

    public <T> List<T> safeEvaluateList(RuntimeExpression expression, Class<T> clazz) {
        if(expression == null)
            return null;
        Object value = expression.evaluate(this);
        if(value instanceof Collection<?> c) {
            return c.stream().map(clazz::cast).collect(Collectors.toCollection(ArrayList::new));
        }
        return null;
    }

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

    public boolean isStopped() {
        return stopped;
    }

    public void stop() {
        stopped = true;
    }

    public SpellRuntime makeChild() {
        return new SpellRuntime(caster, variables, stopped);
    }


}
