package fr.jamailun.ultimatespellsystem.dsl.nodes.type;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

/**
 * Represents a duration.
 * @param amount the quantity of time unit.
 * @param timeUnit the unit to use.
 */
public record Duration(double amount, TimeUnit timeUnit) {

    /**
     * Convert the duration to a raw seconds-count.
     * @return an amount of seconds.
     */
    public double toSeconds() {
        double factor = switch (timeUnit) {
            case NANOSECONDS -> 1E-9;
            case MICROSECONDS -> 1E-6;
            case MILLISECONDS -> 1E-3;
            case SECONDS -> 1;
            case MINUTES -> 60;
            case HOURS -> 3600;
            case DAYS -> 86400;
        };
       return amount * factor;
    }

    /**
     * Adds this duration with another one.
     * @param other the other duration.
     * @return a new instance of duration.
     */
    @Contract("_ -> new")
    public @NotNull Duration add(Duration other) {
        if(other.timeUnit == timeUnit)
            return new Duration(amount + other.amount, timeUnit);
        return new Duration(toSeconds() + other.toSeconds(), TimeUnit.SECONDS);
    }

    /**
     * Subtracts this duration with another one.
     * @param other the other duration.
     * @return a new instance of duration.
     */
    @Contract("_ -> new")
    public @NotNull Duration sub(@NotNull Duration other) {
        return new Duration(Math.max(0, toSeconds() - other.toSeconds()), TimeUnit.SECONDS);
    }

    /**
     * Convert the duration to a raw milliseconds-count.
     * @return an amount of milliseconds.
     */
    public long toMs() {
        return (long) (toSeconds() * 1000);
    }

    /**
     * Convert the duration to a raw ticks-count.
     * @return an amount of ticks.
     */
    public long toTicks() {
        return (long) (toSeconds() * 20);
    }

    /**
     * For debug purposes, get the unit as a nice string.
     * @return a non-null string.
     */
    @Contract(pure = true)
    public @NotNull String niceUnit() {
        String s = amount > 1 ? "s" : "";
        return switch (timeUnit) {
            case NANOSECONDS -> "nanosecond" + s;
            case MICROSECONDS -> "microsecond" + s;
            case MILLISECONDS -> "millisecond" + s;
            case SECONDS -> "second" + s;
            case MINUTES -> "minute" + s;
            case HOURS -> "hour" + s;
            case DAYS -> "day" + s;
        };
    }
}
