package fr.jamailun.ultimatespellsystem.plugin.commands;

import fr.jamailun.ultimatespellsystem.UssLogger;
import fr.jamailun.ultimatespellsystem.api.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.api.spells.Spell;
import fr.jamailun.ultimatespellsystem.api.spells.SpellsManager;
import fr.jamailun.ultimatespellsystem.UssMain;
import fr.jamailun.ultimatespellsystem.api.utils.MultivaluedMap;
import fr.jamailun.ultimatespellsystem.plugin.utils.Pair;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    protected @NotNull List<String> autocompleteWithFlags(int startIndex, @NotNull String @NotNull [] args, @NotNull Map<String, FlagEntry> config) {
        List<String> out = config.keySet().stream()
                .map(s -> "-" + s)
                .collect(Collectors.toCollection(ArrayList::new));

        // Remove arg proposal if it already exists.
        Arrays.stream(args)
                .filter(s -> s.startsWith("-"))
                .forEach(out::remove);

        // Is last one keys ?
        String last = args[args.length - 1];
        if(last.startsWith("-")) {
            return out;
        }

        // We want to find the last key !
        List<String> metArgs = new ArrayList<String>().reversed();
        for(int i = args.length - 2; i >= startIndex; i--) {
            String current = args[i];
            // This was a key, as such we return the config values for that key
            if(current.startsWith("-")) {
                FlagEntry entry = config.get(current.substring(1));
                if(entry == null) return List.of(args[args.length - 1]);

                Pair<List<String>, Boolean> completion = entry.produce(metArgs);
                List<String> list = completion.first();
                if(!completion.second())
                    return list;

                out.addAll(list);
                return out;
            }
            metArgs.add(current);
        }

        // No key ? simply return the allowed keys then.
        return out;
    }

    /**
     * Configuration for flags management
     * @param allowedProducer args[] -> proposal
     */
    protected record FlagEntry(Function<List<String>, Pair<List<String>, Boolean>> allowedProducer) {
        public Pair<List<String>,Boolean> produce(List<String> args) {
            return Objects.requireNonNullElse(allowedProducer().apply(args), Pair.of(Collections.emptyList(), true));
        }
    }
}
