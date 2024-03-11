package fr.jamailun.ultimatespellsystem.bukkit.spells;

import fr.jamailun.ultimatespellsystem.bukkit.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.bukkit.events.EntityCastSpellEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public abstract class Spell {

    private final String name;
    private boolean enabled = true;

    public Spell(String name) {
        this.name = name;
    }

    /**
     * Cast a spell, in a forceful way. An event will be emitted.
     * @param entity the play to cast the spell.
     * @see EntityCastSpellEvent
     */
    public final void castNotCancellable(@NotNull LivingEntity entity) {
        Bukkit.getServer().getPluginManager().callEvent(new EntityCastSpellEvent(entity, this, false));
        castSpell(entity);
    }

    /**
     * Cast a spell, in a cancellable way. An event will be emitted.
     * @param player the play to cast the spell.
     * @see EntityCastSpellEvent
     */
    public final void cast(@NotNull Player player) {
        EntityCastSpellEvent event = new EntityCastSpellEvent(player, this, true);
        Bukkit.getServer().getPluginManager().callEvent(event);
        if (!event.isCancelled())
            castSpell(player);
    }

    protected abstract void castSpell(@NotNull LivingEntity player);


    public final boolean isEnabled() {
        return enabled;
    }

    public final void setEnabled(boolean enabled) {
        this.enabled = enabled;
        UltimateSpellSystem.logInfo("Spell '" + name + "' has been " + (enabled?"enabled":"disabled") + ".");
    }

    public final String getName() {
        return name;
    }

}
