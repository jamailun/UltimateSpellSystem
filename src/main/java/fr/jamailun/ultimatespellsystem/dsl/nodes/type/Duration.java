package fr.jamailun.ultimatespellsystem.dsl.nodes.type;

import java.util.concurrent.TimeUnit;

public record Duration(double amount, TimeUnit timeUnit) {
}
