package fr.jamailun.ultimatespellsystem.plugin.utils;

import org.bukkit.Location;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public record Point(int x, int y, int z) {

    @Contract("_ -> new")
    public static @NotNull Point fromLocation(@NotNull Location location) {
        return new Point(
                location.getBlockX(),
                location.getBlockY(),
                location.getBlockZ()
        );
    }

}
