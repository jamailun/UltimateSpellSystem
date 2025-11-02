package fr.jamailun.ultimatespellsystem.plugin.runner;

import fr.jamailun.ultimatespellsystem.api.entities.SpellEntity;
import fr.jamailun.ultimatespellsystem.api.runner.FunctionsSet;
import fr.jamailun.ultimatespellsystem.api.runner.SpellRuntime;
import fr.jamailun.ultimatespellsystem.api.spells.Spell;
import fr.jamailun.ultimatespellsystem.plugin.entities.BukkitSpellEntity;
import lombok.Getter;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Implementation of Spell Runtime.
 */
@Getter
public final class SpellRuntimeImpl extends AbstractSpellRuntime {

    private final SpellEntity caster;

    /**
     * Create a new context.
     * @param caster bukkit-implementation of the caster.
     * @param spell spell to use.
     */
    public SpellRuntimeImpl(@NotNull LivingEntity caster, @Nullable Spell spell) {
        this(new BukkitSpellEntity(caster), spell);
    }

    /**
     * Create a new context.
     * @param caster the caster to declare.
     * @param spell spell to use.
     */
    public SpellRuntimeImpl(@NotNull SpellEntity caster, @Nullable Spell spell) {
        super(new ExitCode(), spell);
        this.caster = caster;
        variables.set("caster", caster);
        variables.set("console", new Object());
    }

    private SpellRuntimeImpl(@NotNull SpellRuntimeImpl parent, @NotNull SpellEntity caster, boolean inFunction) {
        super(parent, inFunction);
        this.caster = caster;
    }

    @Override
    public @NotNull SpellRuntime makeChild(boolean inFunction) {
        return new SpellRuntimeImpl(this, caster, inFunction);
    }

    @Override
    public @NotNull SpellRuntime makeChildNewCaster(@NotNull SpellEntity newCaster) {
        return new SpellRuntimeImpl(this, newCaster, false);
    }

    @Override
    public @NotNull String toString() {
        return "SpellRuntime[" + caster + ", vars=" + variables() + "]";
    }
}
