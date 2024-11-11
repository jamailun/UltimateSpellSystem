package fr.jamailun.ultimatespellsystem.bukkit.commands;

import fr.jamailun.ultimatespellsystem.api.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.api.bukkit.spells.Spell;
import fr.jamailun.ultimatespellsystem.api.bukkit.spells.SpellsManager;
import fr.jamailun.ultimatespellsystem.bukkit.UssMain;
import org.bukkit.Bukkit;
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

    protected boolean error(@NotNull CommandSender sender, @NotNull String message) {
        sender.sendMessage(UssMain.PREFIX + "§4ERROR | §c" + message);
        return true;
    }

    protected boolean info(@NotNull CommandSender sender, @NotNull String message) {
        sender.sendMessage(UssMain.PREFIX + "§3INFO | §7" + message);
        return true;
    }

    protected boolean success(@NotNull CommandSender sender, @NotNull String message) {
        sender.sendMessage(UssMain.PREFIX + "§aSUCCESS | §f" + message);
        return true;
    }

    protected @NotNull String print(@NotNull Spell spell) {
        return (spell.isEnabled() ? "§a" : "§c") + spell.getName();
    }

}
