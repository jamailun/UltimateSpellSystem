package fr.jamailun.ultimatespellsystem.plugin.utils;

import fr.jamailun.ultimatespellsystem.UssLogger;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Duration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

public final class DurationHelper {
    private DurationHelper() {}

    public static Duration parse(@Nullable String raw, Duration defaultValue) {
        if(raw == null || raw.isBlank()) return defaultValue;

        if(raw.endsWith("s")) {
            return extractValue(raw).map(l -> new Duration(l, TimeUnit.SECONDS)).orElse(defaultValue);
        } else if(raw.endsWith("m")) {
            return extractValue(raw).map(l -> new Duration(l, TimeUnit.MINUTES)).orElse(defaultValue);
        } else if(raw.endsWith("h")) {
            return extractValue(raw).map(l -> new Duration(l, TimeUnit.HOURS)).orElse(defaultValue);
        } else if(raw.endsWith("t")) {
            return extractValue(raw).map(l -> new Duration(l * 50, TimeUnit.MILLISECONDS)).orElse(defaultValue);
        } else {
            UssLogger.logError("Invalid duration suffix in '" + raw + "'.");
            return defaultValue;
        }
    }

    private static Optional<Long> extractValue(@NotNull String raw) {
        try {
            return Optional.of(Long.parseLong(raw.substring(0, raw.length() - 1)));
        } catch(NumberFormatException e) {
            UssLogger.logError("Could not parse duration '" + raw + "' : invalid number");
            return Optional.empty();
        }
    }

}
