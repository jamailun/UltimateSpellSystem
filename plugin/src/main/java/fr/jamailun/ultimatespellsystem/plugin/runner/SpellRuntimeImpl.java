package fr.jamailun.ultimatespellsystem.plugin.runner;

import fr.jamailun.ultimatespellsystem.api.runner.SpellRuntime;
import fr.jamailun.ultimatespellsystem.plugin.entities.BukkitSpellEntity;
import lombok.Getter;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

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
        super(new ExitCode());
        this.caster = caster;
        variables.set("caster", new BukkitSpellEntity(caster));
    }

    private SpellRuntimeImpl(@NotNull SpellRuntimeImpl parent) {
        super(parent);
        this.caster = parent.caster;
    }

    @Override
    public @NotNull SpellRuntime makeChild() {
        return makeChildNewCaster(caster);
    }

    @Override
    public @NotNull SpellRuntime makeChildNewCaster(@NotNull LivingEntity newCaster) {
        return new SpellRuntimeImpl(this);
    }

}
