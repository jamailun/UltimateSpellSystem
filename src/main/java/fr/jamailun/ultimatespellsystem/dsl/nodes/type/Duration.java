package fr.jamailun.ultimatespellsystem.dsl.nodes.type;

import java.util.concurrent.TimeUnit;

public record Duration(double amount, TimeUnit timeUnit) {

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

    public long toTicks() {
        return (long) (toSeconds() * 20);
    }
}
