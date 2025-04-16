package fr.jamailun.ultimatespellsystem.plugin.commands;

import fr.jamailun.ultimatespellsystem.api.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.api.bind.*;
import fr.jamailun.ultimatespellsystem.api.spells.Spell;
import fr.jamailun.ultimatespellsystem.plugin.bind.SpellBindDataImpl;
import fr.jamailun.ultimatespellsystem.plugin.bind.SpellTriggerImpl;
import fr.jamailun.ultimatespellsystem.plugin.runner.nodes.functions.SendAttributeNode;
import fr.jamailun.ultimatespellsystem.plugin.configuration.UssConfig;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Stream;

public class UssCommand extends AbstractCommand {

    private final JavaPlugin plugin;
    private final UssConfig config;

    public UssCommand(@NotNull JavaPlugin plugin, @NotNull UssConfig config) {
        super("uss");
        this.plugin = plugin;
        this.config = config;
    }

    private final static List<String> args_0 = List.of("help", "reload", "list", "cast", "disable", "enable", "bind", "unbind", "bind-check", "purge", "debug");
    private final static List<String> args_0_with_id = List.of("cast", "disable"," enable", "bind");

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length == 0) {
            return error(sender, "Missing argument. Allowed: " + args_0 + ". Do /help for more information.");
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

        // Help
        if("help".equals(arg0)) {
            sendHelp(sender);
            return true;
        }

        // PURGE
        if("purge".equals(arg0)) {
            UltimateSpellSystem.getAnimationsManager().purge();
            int summons = UltimateSpellSystem.getSummonsManager().purgeAll();
            SendAttributeNode.purge();
            Bukkit.getScheduler().cancelTasks(plugin);
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
            if(args.length >= 2) {
                String spellId = args[1];
                UltimateSpellSystem.getItemBinder().unbind(item, spellId);
                return success(sender, "Spell '&e"+spellId+"&r' removed from your main-hand item.");
            }
            UltimateSpellSystem.getItemBinder().unbind(item);
            return success(sender, "Item in main-hand cleared from bound-spells.");
        }

        if("bind-check".equals(arg0)) {
            if (!(sender instanceof Player p)) {
                return error(sender, "You must be a player to " + arg0 + " a spell to an item.");
            }
            ItemStack item = p.getInventory().getItemInMainHand();
            UltimateSpellSystem.getItemBinder().getBindDatas(item).ifPresentOrElse(
                list -> {
                    info(sender, "Bound spells on this item :");
                    for(SpellBindData data : list) {
                        boolean exists = spells().getSpell(data.getSpellId()) != null;
                        String id = (exists?"§a":"§c") + data.getSpellId();
                        info(sender, "- " + id + "&r :");
                        info(sender, "  - Trigger: &e" + data.getTrigger().getTriggersList());
                        info(sender, "  - Cost: &e" + data.getCost());
                        if(data.isLegacy())
                            info(sender, "  - &c[Legacy]");
                    }
                },
                () ->  info(sender, "No spell has been bound to this item.")
            );
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
        if("debug".equals(arg0)) {
            String debug = spell.getDebugString();
            if(sender instanceof Player) // only print in console if player
                UltimateSpellSystem.logInfo("Debug spell [" + spell.getName() + "] : " + debug);
            return info(sender, "SPELL["+spell.getName()+"]=" + debug);
        }

        if("bind".equals(arg0)) { // us bind <spell_id> <cost_type> <cost arg> <trigger>
            if(!(sender instanceof Player p)) {
                return error(sender, "You must be a player to bind a spell to an item.");
            }

            SpellCost cost;
            int costLength;
            if(args.length > 2) {
                SpellCostEntry<?> spellCostEntry = UltimateSpellSystem.getSpellCostRegistry().get(args[2]);
                if(spellCostEntry == null) {
                    return error(p, "Unknown cost-type: '&4" + args[2] + "&r'.");
                }
                int costArgsCount = args.length - 3;
                if(costArgsCount < spellCostEntry.args().size()) {
                    return error(p, "Missing arguments for the cost. Expected args type: " + spellCostEntry.args());
                }
                List<String> costArgs = Arrays.asList(args).subList(3, spellCostEntry.args().size() + 3);
                cost = spellCostEntry.deserialize(costArgs);
                costLength = spellCostEntry.args().size();
            } else {
                cost = config.getDefaultCost();
                costLength = 0;
            }

            // Read trigger
            List<ItemBindTrigger> triggerSteps;
            if(args.length > 3 + costLength) {
                List<String> values = Arrays.asList(args).subList(3 + costLength, args.length);
                triggerSteps = new ArrayList<>(values.size());
                for(String value : values) {
                    try {
                        triggerSteps.add(ItemBindTrigger.valueOf(value.toUpperCase()));
                    } catch(IllegalArgumentException e) {
                        return error(p, "Invalid trigger value: '&4" + value + "&r'.");
                    }
                }
            } else {
                // default trigger
                triggerSteps = new ArrayList<>( config.getDefaultTrigger() );
            }
            SpellTrigger trigger = new SpellTriggerImpl(triggerSteps, cost);
            SpellBindData data = new SpellBindDataImpl(spell, trigger);

            ItemStack item = p.getInventory().getItemInMainHand();
            try {
                UltimateSpellSystem.getItemBinder().bind(item, data);
            } catch(ItemBindException e) {
                return error(sender, e.getMessage());
            }
            return success(sender, "Item bound successfully. Triggers: &7" + triggerSteps);
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
            if("unbind".equalsIgnoreCase(args[0]) && sender instanceof Player p) {
                return UltimateSpellSystem.getItemBinder().getBindDatas(p.getInventory().getItemInMainHand())
                    .orElse(Collections.emptyList())
                    .stream()
                    .map(SpellBindData::getSpellId)
                    .filter(s -> s.contains(arg1))
                    .toList();
            }
        }

        if(args.length == 3) {
            if("bind".equalsIgnoreCase(args[0])) {
                String arg2 = args[2].toLowerCase();
                return UltimateSpellSystem.getSpellCostRegistry().listIds().stream().filter(s -> s.contains(arg2)).toList();
            }
        }

        if(args.length >= 4 && "bind".equals(args[0])) {
            SpellCostEntry<?> costEntry = UltimateSpellSystem.getSpellCostRegistry().get(args[2]);
            if(costEntry == null) return Collections.emptyList();
            // /uss bind <spell> <type> [ARG...]
            int current = args.length - 4;
            String argN = args[args.length - 1].toLowerCase();
            // We are writing an argument
            if(current < costEntry.args().size()) {
                SpellCostArgType type = costEntry.args().get(current);
                List<String> ex = switch(type) {
                    case DOUBLE, INTEGER -> List.of("0", "5", "10");
                    case BOOLEAN -> List.of("true", "false");
                    default -> List.of(argN);
                };
                return ex.stream().filter(s -> s.contains(argN)).toList();
            }
            // triggers !!
            return Arrays.stream(ItemBindTrigger.values())
                .map(t -> t.name().toLowerCase())
                .filter(s -> s.startsWith(argN))
                .toList();
        }

        return Collections.emptyList();
    }

    private @NotNull Stream<String> spellIds() {
        return spells().spellIds().stream();
    }

    private void sendHelp(@NotNull CommandSender sender) {
        info(sender, "Ultimate Spells System command guide :");
        info(sender, "/uss&e help&r: display this help page.");
        info(sender, "/uss&e reload&r: reload the plugin configuration and the spells.");
        info(sender, "/uss&e list&r: list existing spells.");
        info(sender, "/uss&e cast&b <spell_id>&r: cast a specific spell on yourself.");
        info(sender, "/uss&e purge&r: purge all plugin tasks and remove all summoned entities.");
        info(sender, "/uss&e bind&b <spell_id>&f [cost] [trigger]&r: Bind a spell to the item your are currently holding. The cost can have a specific argument. The trigger is a sequence of steps to execute for the spell to be triggered.");
        info(sender, "/uss&e bind-check&r: check if the item you are currently holding is bound to a spell.");
        info(sender, "/uss&e unbind&f [spell_id]&r: unbind a spell from the item you are currently holding. If no spell is specified, remove all of them.");
        info(sender, "/uss&e disable&b <spell_di>&r: disable a spell. The change is not persisted through server stop.");
        info(sender, "/uss&e enable&b <spell_id>&r: re-enable a disabled spell.");
        info(sender, "/uss&e debug&b <spell_id>&r: get the code of a spell. Will also be printed to the console.");
    }

}
