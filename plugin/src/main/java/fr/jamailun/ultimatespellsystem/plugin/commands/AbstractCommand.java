package fr.jamailun.ultimatespellsystem.plugin.commands;

import fr.jamailun.ultimatespellsystem.UssLogger;
import fr.jamailun.ultimatespellsystem.api.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.api.spells.Spell;
import fr.jamailun.ultimatespellsystem.api.spells.SpellsManager;
import fr.jamailun.ultimatespellsystem.UssMain;
import fr.jamailun.ultimatespellsystem.api.utils.MultivaluedMap;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * Abstract command util methods, registration, ...
 */
public abstract class AbstractCommand implements CommandExecutor, TabCompleter {

    public AbstractCommand(@NotNull String command) {
        PluginCommand cmd = Bukkit.getPluginCommand(command);
        if(cmd == null) {
            UssLogger.logError("Could not load command '"+command+"'");
            return;
        }
        cmd.setTabCompleter(this);
        cmd.setExecutor(this);
    }

    protected @NotNull SpellsManager spells() {
        return UltimateSpellSystem.getSpellsManager();
    }

    @SuppressWarnings("deprecation")
    private boolean msg(@NotNull CommandSender sender, @NotNull String message, @NotNull String color) {
        String colored = ChatColor.translateAlternateColorCodes('&', message.replace("&r", color));
        sender.sendMessage(UssMain.PREFIX + colored);
        return true;
    }

    protected boolean error(@NotNull CommandSender sender, @NotNull String message) {
        return msg(sender, "&4ERROR | &c" + message, "&c");
    }

    protected boolean info(@NotNull CommandSender sender, @NotNull String message) {
        return msg(sender, "&3INFO | &7" + message, "&7");
    }

    protected boolean success(@NotNull CommandSender sender, @NotNull String message) {
        return msg(sender, "&aSUCCESS | &f" + message, "&f");
    }

    protected @NotNull String print(@NotNull Spell spell) {
        return (spell.isEnabled() ? "§a" : "§c") + spell.getName();
    }

    protected @Nullable MultivaluedMap<String, String> parseWithFlags(int startIndex,  @NotNull String @NotNull [] args) {
        MultivaluedMap<String, String> map = new MultivaluedMap<>();
        String key = null;
        for(int i = startIndex; i < args.length; i++) {
            String current = args[i];
            if(current.startsWith("-")) {
                key = current.substring(1);
            } else {
                if(key == null) {
                    // Invalid syntax !
                    return null;
                }
                map.put(key, current);
            }
        }
        return map;
    }

    protected @NotNull List<String> autocompleteWithFlags(int startIndex, @NotNull String @NotNull [] args, @NotNull Map<String, Collection<String>> config) {
        // Is last one keys ?
        String last = args[args.length - 1];
        if(last.startsWith("-")) {
            return new ArrayList<>(config.keySet());
        }

        // We want to find the last key !

        List<String> out = new ArrayList<>(config.keySet());
        for(int i = args.length - 2; i >= startIndex; i--) {
            String current = args[i];
            // This was a key, as such we return the config values for that key
            if(current.startsWith("-")) {
                out.addAll(config.getOrDefault(current.substring(1), Collections.emptyList()));
                return out; // we add the proper configured elements.
            }
        }

        // No key ? simply return the allowed keys then.
        return out;
    }

}
