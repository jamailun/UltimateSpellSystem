package fr.jamailun.ultimatespellsystem.api.events;

import fr.jamailun.ultimatespellsystem.api.spells.Spell;
import fr.jamailun.ultimatespellsystem.api.runner.errors.UnreachableRuntimeException;
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

    public BoundSpellCastEvent(@NotNull LivingEntity caster, @NotNull Spell spell, @NotNull ItemStack boundItem, @NotNull Action action) {
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

    /**
     * Action that caused the event.
     */
    public enum Action {
        /**
         * A left click on the air.
         */
        LEFT_CLICK_AIR,
        /**
         * A left click on a block.
         */
        LEFT_CLICK_BLOCK,
        /**
         * A right click on the air.
         */
        RIGHT_CLICK_AIR,
        /**
         * A right click on a block.
         */
        RIGHT_CLICK_BLOCK,
        /**
         * A left click on an entity.
         */
        ATTACK;

        /**
         * Convert a bukkit action to one of the instance action.
         * @param action non-null bukkit action.
         * @return a non-null action.
         */
        public static @NotNull Action convert(@NotNull org.bukkit.event.block.Action action) {
            return switch (action) {
                case LEFT_CLICK_BLOCK -> LEFT_CLICK_BLOCK;
                case RIGHT_CLICK_BLOCK -> RIGHT_CLICK_BLOCK;
                case LEFT_CLICK_AIR -> LEFT_CLICK_AIR;
                case RIGHT_CLICK_AIR -> RIGHT_CLICK_AIR;
                case PHYSICAL -> throw new UnreachableRuntimeException("Cannot be physical.");
            };
        }
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
