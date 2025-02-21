package fr.jamailun.ultimatespellsystem.plugin.runner;

import fr.jamailun.ultimatespellsystem.api.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.api.runner.SpellRuntime;
import fr.jamailun.ultimatespellsystem.api.runner.VariablesSet;
import fr.jamailun.ultimatespellsystem.plugin.entities.BukkitSpellEntity;
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
@Getter
public final class SpellRuntimeImpl extends AbstractSpellRuntime {

    private final LivingEntity caster;

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
    public @NotNull SpellRuntime makeChild() {
        return new SpellRuntimeImpl(caster, variables, exitCode);
    }

}
