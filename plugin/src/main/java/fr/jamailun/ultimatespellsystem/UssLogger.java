package fr.jamailun.ultimatespellsystem;

import lombok.AccessLevel;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

/**
 * Internal access to Bukkit schedule, for the plugin.
 */
public final class UssLogger {

    @Setter(AccessLevel.MODULE)
    private static @NotNull Supplier<Boolean> isDebug = () -> false;

    @SuppressWarnings("deprecation")
    private static void sendMessage(@NotNull String message, @NotNull String color) {
        Bukkit.getConsoleSender().sendMessage(UssMain.PREFIX + ChatColor.translateAlternateColorCodes('&', message.replace("&r", color)));
    }

    /**
     * Log a DEBUG message. Will only be sent if debug mode is enabled in the configuration.
     * @param message the message to print.
     */
    public static void logDebug(@NotNull String message) {
        if(isDebug.get())
            sendMessage("&9DEBUG | &7" + message, "&7");
    }

    /**
     * Log a INFO message.
     * @param message the message to print.
     */
    public static void logInfo(@NotNull String message) {
        sendMessage("&3INFO | &f" + message, "&f");
    }

    /**
     * Log a WARN message.
     * @param message the message to print.
     */
    public static void logWarning(@NotNull String message) {
        sendMessage("&6WARN | &e" + message, "&e");
    }

    /**
     * Log a ERROR message.
     * @param message the message to print.
     */
    public static void logError(@NotNull String message) {
        sendMessage("&4ERROR | &c" + message, "&c");
    }

    /**
     * Log a ERROR message with an exception instance.
     * @param message the message to print.
     * @param throwable the exception to print.
     */
    public static void logError(@NotNull String message, @NotNull Throwable throwable) {
        logError(message);
        throwable.printStackTrace();
    }

}
