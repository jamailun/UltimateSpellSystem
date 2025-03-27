package fr.jamailun.ultimatespellsystem.api.bind;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public enum ItemBindTrigger {

    ATTACK,
    DROP,

    LEFT_CLICK,
    LEFT_CLICK_AIR,
    LEFT_CLICK_BLOCK,
    RIGHT_CLICK,
    RIGHT_CLICK_AIR,
    RIGHT_CLICK_BLOCK,

    SNEAK;

    private final Set<ItemBindTrigger> children;

    ItemBindTrigger() {
        this.children = Collections.emptySet();
    }
    ItemBindTrigger(ItemBindTrigger... children) {
        this.children = Set.copyOf(List.of(children));
    }


}
