package fr.jamailun.ultimatespellsystem.api.bind;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * A
 */
public enum ItemBindTrigger {

    ATTACK(0x0000),
    DROP(0x0001),

    LEFT_CLICK_AIR(0x0a01),
    LEFT_CLICK_BLOCK(0x0a02),
    LEFT_CLICK(0x0a00, LEFT_CLICK_BLOCK, LEFT_CLICK_AIR),
    RIGHT_CLICK_AIR(0x0b01),
    RIGHT_CLICK_BLOCK(0x0b02),
    RIGHT_CLICK(0x0b00, RIGHT_CLICK_BLOCK, RIGHT_CLICK_AIR),

    SNEAK(0x0c00);

    @Getter private final int code;
    private final Set<ItemBindTrigger> children;

    ItemBindTrigger(int code) {
        this.code = code;
        this.children = Collections.emptySet();
    }
    ItemBindTrigger(int code, @NotNull ItemBindTrigger... children) {
        this.code = code;
        this.children = Set.copyOf(List.of(children));
    }

    public static @NotNull ItemBindTrigger fromCode(int code) {
        return Arrays.stream(values())
            .filter(t -> t.code == code)
            .findFirst().orElseThrow();
    }

}
