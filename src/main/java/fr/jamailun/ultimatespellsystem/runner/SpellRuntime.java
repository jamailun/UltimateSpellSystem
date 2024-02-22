package fr.jamailun.ultimatespellsystem.runner;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SpellRuntime {

    private final VariablesSet variables = new VariablesSet();
    private final Player caster;

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
        Object value = expression.evaluate(this);
        return clazz.cast(value);
    }

}
