package fr.jamailun.ultimatespellsystem.bukkit.events;

import fr.jamailun.ultimatespellsystem.bukkit.spells.SpellDefinition;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Event called every time a player cast a USS spell.
 */
public class BoundSpellCast extends Event implements Cancellable {

    private final Player player;
    private final SpellDefinition spell;
    private final ItemStack boundItem;
    private boolean cancelled = false;
    private boolean interactionCancelled = true;

    public BoundSpellCast(Player player, SpellDefinition spell, ItemStack boundItem) {
        this.player = player;
        this.spell = spell;
        this.boundItem = boundItem;
    }

    public @NotNull SpellDefinition getSpell() {
        return spell;
    }

    public @NotNull Player getPlayer() {
        return player;
    }
    public @NotNull ItemStack getBoundItem() {
        return boundItem;
    }

    private static final HandlerList HANDLERS = new HandlerList();
    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public boolean isInteractionCancelled() {
        return interactionCancelled;
    }

    public void setInteractionCancelled(boolean b) {
        this.interactionCancelled = b;
    }
}
