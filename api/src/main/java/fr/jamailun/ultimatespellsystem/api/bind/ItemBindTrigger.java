package fr.jamailun.ultimatespellsystem.api.bind;

import org.bukkit.event.block.Action;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

    /**
     * Convert a Bukkit interact action, to a custom bind-trigger.
     * @param action the action.
     * @return null if the provided action is {@link Action#PHYSICAL PHYSICAL}.
     */
    public static @Nullable ItemBindTrigger convert(@NotNull Action action) {
        return switch (action) {
            case LEFT_CLICK_BLOCK -> LEFT_CLICK_BLOCK;
            case RIGHT_CLICK_BLOCK -> RIGHT_CLICK_BLOCK;
            case LEFT_CLICK_AIR -> LEFT_CLICK_AIR;
            case RIGHT_CLICK_AIR -> RIGHT_CLICK_AIR;
            default -> null;
        };
    }
}
