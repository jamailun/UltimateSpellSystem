package fr.jamailun.ultimatespellsystem.bukkit.runner;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SpellRuntime {

    private final VariablesSet variables = new VariablesSet();
    private final Player caster;
    private boolean stopped = false;

    public SpellRuntime(@NotNull Player caster) {
        this.caster = caster;
        variables.set("caster", caster);
    }

    public Player getCaster() {
        return caster;
    }

    public VariablesSet variables() {
        return variables;
    }

    public <T> T safeEvaluate(RuntimeExpression expression, Class<T> clazz) {
        if(expression == null)
            return null;
        Object value = expression.evaluate(this);
        return clazz.cast(value);
    }

    public boolean isStopped() {
        return stopped;
    }

    public void stop() {
        stopped = true;
    }

}
