package fr.jamailun.examples.utils;

import fr.jamailun.examples.ExamplePlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

public final class DurationHelper {
    private DurationHelper() {}

    public static Duration parse(@Nullable String raw, Duration defaultValue) {
        if(raw == null || raw.isBlank()) return defaultValue;

        if (raw.endsWith("ms")) {
            return extractValue(raw).map(l -> Duration.of(l, ChronoUnit.MILLIS)).orElse(defaultValue);
        } else if(raw.endsWith("s")) {
            return extractValue(raw).map(l -> Duration.of(l, ChronoUnit.SECONDS)).orElse(defaultValue);
        } else if(raw.endsWith("m")) {
            return extractValue(raw).map(l -> Duration.of(l, ChronoUnit.MINUTES)).orElse(defaultValue);
        } else if(raw.endsWith("h")) {
            return extractValue(raw).map(l -> Duration.of(l, ChronoUnit.HOURS)).orElse(defaultValue);
        } else if(raw.endsWith("t")) {
            return extractValue(raw).map(l -> Duration.of(l * 50, ChronoUnit.MILLIS)).orElse(defaultValue);
        } else {
            ExamplePlugin.error("Invalid duration suffix in '" + raw + "'.");
            return defaultValue;
        }
    }

    private static Optional<Long> extractValue(@NotNull String raw) {
        try {
            return Optional.of(Long.parseLong(raw.substring(0, raw.length() - 1)));
        } catch(NumberFormatException e) {
            ExamplePlugin.error("Could not parse duration '" + raw + "' : invalid number");
            return Optional.empty();
        }
    }

}
