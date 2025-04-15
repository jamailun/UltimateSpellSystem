package fr.jamailun.ultimatespellsystem.api.events;

import fr.jamailun.ultimatespellsystem.api.bind.ItemBindTrigger;
import fr.jamailun.ultimatespellsystem.api.bind.SpellBindData;
import lombok.Getter;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Event called every time an entity casts a USS spell.
 */
@Getter
public class BoundSpellCastEvent extends BindingEvent implements Cancellable {

    private final LivingEntity caster;
    private boolean cancelled = false;
    private boolean interactionCancelled = true;
    private final ItemBindTrigger lastTrigger;

    /**
     * A new event for an entity casting a spell.
     * @param caster the caster.
     * @param data the selected spell data.
     * @param boundItem item used for the cast.
     * @param lastTrigger last action.
     */
    public BoundSpellCastEvent(@NotNull LivingEntity caster, @NotNull SpellBindData data, @NotNull ItemStack boundItem, @NotNull ItemBindTrigger lastTrigger) {
        super(data, boundItem);
        this.caster = caster;
        this.lastTrigger = lastTrigger;
    }

    /**
     * Get the player casting the spell.
     * @return a non-null player, holding an item bound to a spell.
     */
    public @NotNull LivingEntity getCaster() {
        return caster;
    }

    public @NotNull ItemBindTrigger getLastTrigger() {
        return lastTrigger;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    /**
     * Check is the underlying interaction with the item is cancelled.
     * @return true if the interaction has been cancelled.
     */
    public boolean isInteractionCancelled() {
        return interactionCancelled;
    }

    /**
     * Set the cancel-state of the underlying interaction.
     * @param b the new state.
     */
    public void setInteractionCancelled(boolean b) {
        this.interactionCancelled = b;
    }


    private static final HandlerList HANDLERS = new HandlerList();
    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    /**
     * Boilerplate
     * @return handlers.
     */
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
