package fr.jamailun.ultimatespellsystem.plugin.spells;

import fr.jamailun.ultimatespellsystem.api.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.api.entities.SpellEntity;
import fr.jamailun.ultimatespellsystem.api.runner.SpellRuntime;
import fr.jamailun.ultimatespellsystem.api.spells.Spell;
import fr.jamailun.ultimatespellsystem.api.events.EntityCastSpellEvent;
import fr.jamailun.ultimatespellsystem.plugin.entities.BukkitSpellEntity;
import fr.jamailun.ultimatespellsystem.plugin.runner.SpellRuntimeImpl;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * A spell is something that can be done by a caster.
 */
public abstract class AbstractSpell implements Spell {

    protected String name;
    private boolean enabled = true;

    public AbstractSpell(@NotNull String name) {
        this.name = name;
    }

    @Override
    public final boolean castNotCancellable(@NotNull LivingEntity caster) {
        return castNotCancellable(new BukkitSpellEntity(caster));
    }

    @Override
    public final boolean castNotCancellable(@NotNull SpellEntity entity, @Nullable SpellRuntime runtime) {
        Bukkit.getServer().getPluginManager().callEvent(new EntityCastSpellEvent(entity, this, false));
        return castSpell(entity, Objects.requireNonNullElse(runtime, new SpellRuntimeImpl(entity)));
    }

    @Override
    public final boolean cast(@NotNull Player player) {
        SpellEntity spellCaster = new BukkitSpellEntity(player);
        EntityCastSpellEvent event = new EntityCastSpellEvent(spellCaster, this, true);
        Bukkit.getServer().getPluginManager().callEvent(event);
        if (!event.isCancelled())
            return castSpell(spellCaster, new SpellRuntimeImpl(spellCaster));
        return false;
    }

    /**
     * Cast the spell.
     * @param caster the non-null caster.
     * @param runtime the runtime to use.
     * @return {@code true} if the spell cast properly. If {@code false}, then the spell has been dropped.
     */
    protected abstract boolean castSpell(@NotNull SpellEntity caster, @NotNull SpellRuntime runtime);

    @Override
    public final boolean isEnabled() {
        return enabled;
    }

    @Override
    public final void setEnabled(boolean enabled) {
        this.enabled = enabled;
        UltimateSpellSystem.logInfo("Spell '" + name + "' has been " + (enabled?"enabled":"disabled") + ".");
    }

    @Override
    public final @NotNull String getName() {
        return name;
    }

}
