package fr.jamailun.ultimatespellsystem.plugin.spells;

import fr.jamailun.ultimatespellsystem.api.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.api.spells.Spell;
import fr.jamailun.ultimatespellsystem.api.events.EntityCastSpellEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * A spell is something that can be done by a caster.
 */
public abstract class AbstractSpell implements Spell {

    private final String name;
    private boolean enabled = true;

    public AbstractSpell(@NotNull String name) {
        this.name = name;
    }

    @Override
    public final boolean castNotCancellable(@NotNull LivingEntity entity) {
        Bukkit.getServer().getPluginManager().callEvent(new EntityCastSpellEvent(entity, this, false));
        return castSpell(entity);
    }

    @Override
    public final boolean cast(@NotNull Player player) {
        EntityCastSpellEvent event = new EntityCastSpellEvent(player, this, true);
        Bukkit.getServer().getPluginManager().callEvent(event);
        if (!event.isCancelled())
            return castSpell(player);
        return false;
    }

    /**
     * Cast the spell.
     * @param player the caster.
     * @return {@code true} if the spell cast properly. If {@code false}, then the spell has been dropped.
     */
    protected abstract boolean castSpell(@NotNull LivingEntity player);

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
