package fr.jamailun.examples.commands;

import fr.jamailun.examples.citizens.CasterTrait;
import fr.jamailun.ultimatespellsystem.api.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.api.spells.Spell;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

/**
 * Cast a spell on a Citizens {@link NPC}.
 */
public class NpcCastCommand implements CommandExecutor, TabCompleter {

    public NpcCastCommand() {
        String command = "test-cast-npc";
        PluginCommand cm = Objects.requireNonNull(Bukkit.getPluginCommand(command), "Command not found: '" + command + "'.");
        cm.setExecutor(this);
        cm.setTabCompleter(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        NPC npc = CitizensAPI.getDefaultNPCSelector().getSelected(sender);
        if(npc == null) {
            sender.sendMessage("§cSelect an NPC first !");
            return true;
        }

        if(args.length == 0) {
            sender.sendMessage("§cMissing argument: spell ID.");
            return true;
        }

        String spellId = args[0];
        Spell spell = UltimateSpellSystem.getSpellsManager().getSpell(spellId);
        if(spell == null) {
            sender.sendMessage("§cUnknown spell ID: '" + spellId + "'.");
            return true;
        }

        npc.getOrAddTrait(CasterTrait.class).cast(spell);
        sender.sendMessage("§aNPC casted the spell.");
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(args.length == 1) {
            String arg0 = args[0].toLowerCase();
            return UltimateSpellSystem.getSpellsManager().spellIds().stream()
                    .filter(sid -> sid.toLowerCase().startsWith(arg0))
                    .toList();
        }
        return List.of();
    }
}
