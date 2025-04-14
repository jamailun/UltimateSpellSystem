package fr.jamailun.ultimatespellsystem.api.bind;

import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * A step required in a spell trigger.
 */
public enum ItemBindTrigger {

    ATTACK,
    DROP,

    LEFT_CLICK_AIR,
    LEFT_CLICK_BLOCK,
    LEFT_CLICK(LEFT_CLICK_BLOCK, LEFT_CLICK_AIR),

    RIGHT_CLICK_AIR,
    RIGHT_CLICK_BLOCK,
    RIGHT_CLICK(RIGHT_CLICK_BLOCK, RIGHT_CLICK_AIR),

    SNEAK;

    private final Set<ItemBindTrigger> children;

    ItemBindTrigger() {
        this.children = Collections.emptySet();
    }
    ItemBindTrigger(@NotNull ItemBindTrigger... children) {
        this.children = Set.copyOf(List.of(children));
    }

    public boolean matches(@NotNull ItemBindTrigger other) {
        return other == this || children.contains(other);
    }
}
