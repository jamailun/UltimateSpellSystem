package fr.jamailun.ultimatespellsystem.api.bind;

import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * A step required in a spell trigger.
 */
public enum ItemBindTrigger {

    /**
     * Attack on an entity.
     */
    ATTACK,

    /**
     * Left-click in the air.
     */
    LEFT_CLICK_AIR,
    /**
     * Left-click on a block.
     */
    LEFT_CLICK_BLOCK,
    /**
     * Any left-click (no attach though).
     */
    LEFT_CLICK(LEFT_CLICK_BLOCK, LEFT_CLICK_AIR),

    /**
     * Right-click in the air.
     */
    RIGHT_CLICK_AIR,
    /**
     * Right-click on a block.
     */
    RIGHT_CLICK_BLOCK,
    /**
     * Any right-click.
     */
    RIGHT_CLICK(RIGHT_CLICK_BLOCK, RIGHT_CLICK_AIR),

    /**
     * Player sneaking.
     */
    SNEAK;

    private final Set<ItemBindTrigger> children;

    ItemBindTrigger() {
        this.children = Collections.emptySet();
    }
    ItemBindTrigger(@NotNull ItemBindTrigger... children) {
        this.children = Set.copyOf(List.of(children));
    }

    /**
     * Test if this triggers matches another one.
     * @param other the other trigger.
     * @return true if triggers are equals, or if the arguments is contained in this instance.
     */
    public boolean matches(@NotNull ItemBindTrigger other) {
        return other == this || children.contains(other);
    }
}
