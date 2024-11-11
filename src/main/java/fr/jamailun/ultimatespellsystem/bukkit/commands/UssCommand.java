package fr.jamailun.ultimatespellsystem.bukkit.commands;

import fr.jamailun.ultimatespellsystem.api.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.api.bukkit.bind.ItemBindException;
import fr.jamailun.ultimatespellsystem.api.bukkit.spells.Spell;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class UssCommand extends AbstractCommand {

    public UssCommand() {
        super("uss");
    }

    private final static List<String> args_0 = List.of("reload", "list", "cast", "disable", "enable", "bind", "unbind", "bind-check", "purge");
    private final static List<String> args_0_with_id = List.of("cast", "disable"," enable", "bind");
    private final static List<String> args_boolean = List.of("true", "false");

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length == 0) {
            return error(sender, "Missing argument. Allowed: " + args_0);
        }
        String arg0 = args[0].toLowerCase();
        if(!args_0.contains(arg0))
            return error(sender, "Unknown argument '" + args[0] + "'. Allowed: " + args_0);

        // RELOAD
        if("reload".equals(arg0)) {
            UltimateSpellSystem.reloadConfiguration();
            spells().reloadSpells();
            return success(sender, "Successfully reloaded configuration and " + spells().spellIds().size() + " spells.");
        }

        // PURGE
        if("purge".equals(arg0)) {
            UltimateSpellSystem.getAnimationsManager().purge();
            int summons = UltimateSpellSystem.getSummonsManager().purgeAll();
            return success(sender, "Purged summons : §e" + summons + "§f.");
        }

        // LIST
        if("list".equals(arg0)) {
            List<Spell> spells = spells().spells();
            if(spells.isEmpty()) {
                return info(sender, "Aucun spell n'a été configuré.");
            }
            info(sender, "List des " + spells.size() + " spells:");
            for(Spell spell : spells) {
                info(sender, "- " + print(spell));
            }
            return true;
        }

        // UNBIND
        if("unbind".equals(arg0)) {
            if (!(sender instanceof Player p)) {
                return error(sender, "You must be a player to " + arg0 + " a spell to an item.");
            }
            ItemStack item = p.getInventory().getItemInMainHand();
            UltimateSpellSystem.getItemBinder().unbind(item);
            return success(sender, "Item-in hand unbound.");
        }

        if("bind-check".equals(arg0)) {
            if (!(sender instanceof Player p)) {
                return error(sender, "You must be a player to " + arg0 + " a spell to an item.");
            }
            ItemStack item = p.getInventory().getItemInMainHand();
            Optional<String> s = UltimateSpellSystem.getItemBinder().tryFindBoundSpell(item);
            if(s.isPresent()) {
                String sid = s.get();
                boolean exists = spells().getSpell(sid) != null;
                boolean destroy = UltimateSpellSystem.getItemBinder().hasDestroyKey(item);
                info(sender, "Spell " + (exists?"§a":"§c") + sid + "§7 is present on the item" + (destroy ? " §3[Destroyable]" : "") + "§7.");
            } else {
                info(sender, "No spell has been bound to this item.");
            }
            return true;
        }

        // From here, another argument is required.
        if(args.length == 1) {
            return error(sender, "Syntax is /" + label + " " + args[0] + " §4<id>");
        }
        String id = args[1].toLowerCase();
        Spell spell = spells().getSpell(id);
        if(spell == null) {
            return error(sender, "Unknown spell ID '" + id + "'. Do §7/"+label+" list§c to obtain the list of existing spells.");
        }

        // DISABLE && ENABLE
        if("disable".equals(arg0)) {
            spell.setEnabled(false);
            return success(sender, "Successfully disabled " + id + ".");
        }
        if("enable".equals(arg0)) {
            spell.setEnabled(true);
            return success(sender, "Successfully enabled " + id + ".");
        }

        if("bind".equals(arg0)) {
            if(!(sender instanceof Player p)) {
                return error(sender, "You must be a player to " + arg0 + " a spell to an item.");
            }
            boolean destroy = false;
            if(args.length > 2) {
                destroy = Boolean.parseBoolean(args[2]);
            }
            ItemStack item = p.getInventory().getItemInMainHand();
            try {
                UltimateSpellSystem.getItemBinder().bind(item, spell, destroy);
            } catch(ItemBindException e) {
                return error(sender, e.getMessage());
            }
            return success(sender, "Item bound successfully.");
        }

        // Cast
        if("cast".equals(arg0)) {
            Player player;
            if(args.length > 2) {
                String playerName = args[2];
                player = Bukkit.getPlayer(playerName);
                if(player == null) {
                    return error(sender, "Unknown player: '" + playerName + "'.");
                }
            } else {
                if(sender instanceof Player p) {
                    player = p;
                } else {
                    return error(sender, "Must be a player to cast a spell.");
                }
            }
            info(sender, "Casting spell on " + (sender.equals(player) ? "yourself" : args[2]) + ".");
            try {
                spell.cast(player);
            } catch (RuntimeException e) {
                error(sender, "An error occurred. " + e.getClass().getSimpleName() + " : " + e.getMessage());
                e.printStackTrace();
            }
            return true;
        }

        throw new RuntimeException("Unreachable exception.");
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length == 1) {
            String arg0 = args[0].toLowerCase();
            return args_0.stream().filter(a -> a.contains(arg0)).toList();
        }

        if(args.length == 2) {
            String arg1 = args[1].toLowerCase();
            if(args_0_with_id.contains(args[0])) {
                return spellIds().filter(s -> s.contains(arg1)).toList();
            }
        }

        if(args.length == 3) {
            if("bind".equalsIgnoreCase(args[0])) {
                String arg2 = args[2].toLowerCase();
                return args_boolean.stream().filter(s -> s.contains(arg2)).toList();
            }
        }

        return Collections.emptyList();
    }

    private @NotNull Stream<String> spellIds() {
        return spells().spellIds().stream();
    }

}
