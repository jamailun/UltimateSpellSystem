package fr.jamailun.ultimatespellsystem.plugin.utils;

import org.jetbrains.annotations.NotNull;

public record Pair<A,B>(@NotNull A first, @NotNull B second) {
    public static <A, B> @NotNull Pair<A, B> of(@NotNull A a, @NotNull B b) {
        return new Pair<>(a, b);
    }
}
