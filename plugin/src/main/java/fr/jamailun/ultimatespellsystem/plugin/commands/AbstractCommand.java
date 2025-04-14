package fr.jamailun.ultimatespellsystem.plugin.commands;

import fr.jamailun.ultimatespellsystem.api.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.api.spells.Spell;
import fr.jamailun.ultimatespellsystem.api.spells.SpellsManager;
import fr.jamailun.ultimatespellsystem.UssMain;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

/**
 * Abstract command util methods, registration, ...
 */
public abstract class AbstractCommand implements CommandExecutor, TabCompleter {

    public AbstractCommand(@NotNull String command) {
        PluginCommand cmd = Bukkit.getPluginCommand(command);
        if(cmd == null) {
            UltimateSpellSystem.logError("Could not load command '"+command+"'");
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

}
