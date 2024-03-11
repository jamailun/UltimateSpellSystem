package fr.jamailun.ultimatespellsystem.bukkit.events;

import fr.jamailun.ultimatespellsystem.bukkit.runner.errors.UnreachableRuntimeException;
import fr.jamailun.ultimatespellsystem.bukkit.spells.Spell;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Event called every time a player cast a USS spell.
 */
public class BoundSpellCastEvent extends BindingEvent implements Cancellable {

    private final LivingEntity caster;
    private boolean cancelled = false;
    private boolean interactionCancelled = true;
    private final Action action;

    public BoundSpellCastEvent(LivingEntity caster, Spell spell, ItemStack boundItem, Action action) {
        super(spell, boundItem);
        this.caster = caster;
        this.action = action;
    }

    /**
     * Get the player casting the spell.
     * @return a non-null player, holding an item bound to a spell.
     */
    public @NotNull LivingEntity getCaster() {
        return caster;
    }

    private static final HandlerList HANDLERS = new HandlerList();
    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public @NotNull Action getAction() {
        return action;
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

    public enum Action {
        LEFT_CLICK_AIR,
        LEFT_CLICK_BLOCK,
        RIGHT_CLICK_AIR,
        RIGHT_CLICK_BLOCK,
        ATTACK;

        public static Action convert(org.bukkit.event.block.Action action) {
            return switch (action) {
                case LEFT_CLICK_BLOCK -> LEFT_CLICK_BLOCK;
                case RIGHT_CLICK_BLOCK -> RIGHT_CLICK_BLOCK;
                case LEFT_CLICK_AIR -> LEFT_CLICK_AIR;
                case RIGHT_CLICK_AIR -> RIGHT_CLICK_AIR;
                case PHYSICAL -> throw new UnreachableRuntimeException("Cannot be physical.");
            };
        }
    }
}
