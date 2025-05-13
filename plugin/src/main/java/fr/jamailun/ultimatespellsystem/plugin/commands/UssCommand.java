package fr.jamailun.ultimatespellsystem.plugin.commands;

import fr.jamailun.ultimatespellsystem.UssLogger;
import fr.jamailun.ultimatespellsystem.api.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.api.bind.*;
import fr.jamailun.ultimatespellsystem.api.providers.AlliesProvider;
import fr.jamailun.ultimatespellsystem.api.providers.JavaFunctionProvider;
import fr.jamailun.ultimatespellsystem.api.providers.ScopeProvider;
import fr.jamailun.ultimatespellsystem.api.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.api.spells.Spell;
import fr.jamailun.ultimatespellsystem.api.utils.MultivaluedMap;
import fr.jamailun.ultimatespellsystem.dsl.UltimateSpellSystemDSL;
import fr.jamailun.ultimatespellsystem.dsl.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Duration;
import fr.jamailun.ultimatespellsystem.plugin.bind.SpellBindDataImpl;
import fr.jamailun.ultimatespellsystem.plugin.bind.SpellTriggerImpl;
import fr.jamailun.ultimatespellsystem.plugin.runner.nodes.functions.SendAttributeNode;
import fr.jamailun.ultimatespellsystem.plugin.configuration.UssConfig;
import fr.jamailun.ultimatespellsystem.plugin.utils.DurationHelper;
import fr.jamailun.ultimatespellsystem.plugin.utils.Pair;
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

    private final static List<String> args_0 = List.of("help", "status", "evaluate", "reload", "list", "cast", "disable", "enable", "bind", "unbind", "bind-check", "purge", "debug");
    private final static List<String> args_0_with_id = List.of("cast", "disable"," enable", "bind");

    private final static Map<String, FlagEntry> BIND_CONFIG = Map.of(
            "cost", flagEntryCost(),
            "trigger", flagEntryTrigger(),
            "cooldown", flagEntryCooldown()
    );

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

        if("status".equals(arg0)) {
            info(sender, "USS &a"+UltimateSpellSystem.getVersion()+"&r status :");

            // Allies
            var alliesKeys = AlliesProvider.instance().getKeys();
            info(sender, "&eAllies:&r " + alliesKeys);
            info(sender, "&eFunctions count:&r " + JavaFunctionProvider.instance().getKeys().size());
            info(sender, "&eScopes count:&r " + ScopeProvider.instance().getKeys().size());
            return true;
        }

        if("evaluate".equals(arg0)) {
            if(!(sender instanceof Player player))
                return error(sender, "you must be a player.");

            // Read
            StringJoiner joiner = new StringJoiner(" ");
            for(int i = 1; i < args.length; i++)
                joiner.add(args[i]);
            String code = joiner.toString();
            if(code.startsWith("\"") && code.endsWith("\""))
                code = code.substring(1, code.length() - 1);
            info(sender, "Expression: &f" + code);

            // Parse
            try {
                ExpressionNode raw = UltimateSpellSystemDSL.parseExpression(code);
                RuntimeExpression expression = UltimateSpellSystem.getExternalExecutor().handleImplementation(raw);
                Object out = expression.evaluate(UltimateSpellSystem.getExternalExecutor().generateRuntime(player));

                return success(sender, "Output value : &e" + out);
            } catch(Exception e) {
                return error(sender, "Could not evaluate expression. " + e.getMessage());
            }
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
            return error(sender, "Unknown spell ID '" + id + "'. Do &7/"+label+" list&c to obtain the list of existing spells.");
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
                UssLogger.logInfo("Debug spell [" + spell.getName() + "] : " + debug);
            return info(sender, "SPELL["+spell.getName()+"]=" + debug);
        }

        if("bind".equals(arg0)) { // us bind <spell_id> --FLAGS(cooldown, trigger, ...)
            if(!(sender instanceof Player p)) {
                return error(sender, "You must be a player to bind a spell to an item.");
            }
            MultivaluedMap<String, String> flagsArgs = parseWithFlags(2, args);
            if(flagsArgs == null) {
                return error(sender, "Invalid syntax. Expected '--<flag> <value...>', with the following flags: " + BIND_CONFIG.keySet());
            }

            // Cost
            SpellCost cost;
            if(flagsArgs.containsKey("cost")) {
                List<String> costArgs = new ArrayList<>(Objects.requireNonNull(flagsArgs.get("cost")));
                SpellCostEntry<?> spellCostEntry = UltimateSpellSystem.getSpellCostRegistry().get(costArgs.getFirst());
                costArgs.removeFirst();
                if(spellCostEntry == null) {
                    return error(p, "Unknown cost-type: '&4" + args[2] + "&r'.");
                }
                int costArgsCount = costArgs.size() - 1;
                if(costArgsCount < spellCostEntry.args().size()) {
                    return error(p, "Missing arguments for the cost. Expected args type: " + spellCostEntry.args());
                }
                cost = spellCostEntry.deserialize(costArgs);
            } else {
                cost = config.getDefaultCost();
            }

            // Trigger
            List<ItemBindTrigger> triggerSteps = new ArrayList<>();
            if(flagsArgs.containsKey("trigger")) {
                for(String value : Objects.requireNonNull(flagsArgs.get("trigger"))) {
                    try {
                        triggerSteps.add(ItemBindTrigger.valueOf(value.toUpperCase()));
                    } catch(IllegalArgumentException e) {
                        return error(p, "Invalid trigger value: '&4" + value + "&r'.");
                    }
                }
            } else {
                triggerSteps.addAll( config.getDefaultTrigger() );
            }

            // Cooldown
            Duration cooldown;
            if(flagsArgs.containsKey("cooldown")) {
                List<String> cdArgs = Objects.requireNonNull(flagsArgs.get("cooldown"));
                cooldown = DurationHelper.parse(cdArgs.getFirst(), null);
            } else {
                cooldown = null;
            }

            SpellTrigger trigger = new SpellTriggerImpl(triggerSteps, cost);
            SpellBindData data = new SpellBindDataImpl(spell, trigger, cooldown);

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
                UssLogger.logError("Error on player cast via command.", e);
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

        if(args.length >= 3 && "bind".equals(args[0])) {
            String argN = args[args.length - 1].toLowerCase();
            return autocompleteWithFlags(2, args, BIND_CONFIG).stream()
                    .filter(s -> s.contains(argN))
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
        info(sender, "/uss&e status&r: get the plugin status.");
        info(sender, "/uss&e list&r: list existing spells.");
        info(sender, "/uss&e cast&b <spell_id>&r: cast a specific spell on yourself.");
        info(sender, "/uss&e purge&r: purge all plugin tasks and remove all summoned entities.");
        info(sender, "/uss&e bind&b <spell_id>&f [cost] [trigger]&r: Bind a spell to the item your are currently holding. The cost can have a specific argument. The trigger is a sequence of steps to execute for the spell to be triggered.");
        info(sender, "/uss&e bind-check&r: check if the item you are currently holding is bound to a spell.");
        info(sender, "/uss&e unbind&f [spell_id]&r: unbind a spell from the item you are currently holding. If no spell is specified, remove all of them.");
        info(sender, "/uss&e disable&b <spell_di>&r: disable a spell. The change is not persisted through server stop.");
        info(sender, "/uss&e enable&b <spell_id>&r: re-enable a disabled spell.");
        info(sender, "/uss&e debug&b <spell_id>&r: get the code of a spell. Will also be printed to the console.");
        info(sender, "/uss&e evaluate&b <USS CODE>&r: evaluate an expression, with yourself as the caster.");
    }

    private static @NotNull FlagEntry flagEntryCost() {
        return new FlagEntry(args -> {
            if(args.isEmpty()) return Pair.of(
                    new ArrayList<>(UltimateSpellSystem.getSpellCostRegistry().listIds()),
                    false
            );

            SpellCostEntry<?> entry = UltimateSpellSystem.getSpellCostRegistry().get(args.getFirst());
            if(entry == null || args.size() > entry.args().size()) return null;
            return switch(entry.args().get(args.size() - 1)) {
                case DOUBLE, INTEGER -> Pair.of(List.of("0", "5", "10"), false);
                case BOOLEAN -> Pair.of(List.of("true", "false"), false);
                default -> Pair.of(List.of(args.getLast(), "foo"), false);
            };
        });
    }

    private static @NotNull FlagEntry flagEntryCooldown() {
        return new FlagEntry(args -> {
            if(!args.isEmpty()) return null;
            return Pair.of(List.of("1m", "5s"), false);
        });
    }

    private static @NotNull FlagEntry flagEntryTrigger() {
        return new FlagEntry(a -> {
            boolean first = a.isEmpty(); // if flag is here, at least need one :)
            return Pair.of(Arrays.stream(ItemBindTrigger.values()).map(b->b.name().toLowerCase()).toList(), !first);
        });
    }

}
