package fr.jamailun.examples.commands;

import fr.jamailun.examples.citizens.CasterTrait;
import fr.jamailun.examples.citizens.SpellCastRule;
import fr.jamailun.examples.utils.DurationHelper;
import fr.jamailun.ultimatespellsystem.api.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.api.spells.Spell;
import fr.jamailun.ultimatespellsystem.dsl.UltimateSpellSystemDSL;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.stream.IntStream;

public class SpellCasterCommand implements CommandExecutor, TabCompleter {

    private static final List<String> ARGS = List.of("help", "add", "list", "remove", "cast");

    public SpellCasterCommand() {
        PluginCommand command = Objects.requireNonNull(Bukkit.getPluginCommand("spellcaster"));
        command.setExecutor(this);
        command.setTabCompleter(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if(args.length > 0 && "help".equalsIgnoreCase(args[0])) {
            sendHelp(sender);
            return true;
        }

        // Always need an NPC
        NPC npc = CitizensAPI.getDefaultNPCSelector().getSelected(sender);
        if(npc == null) {
            sender.sendMessage("§cYou need to select an NPC first.");
            return true;
        }

        // List
        if(args.length == 0 || "list".equalsIgnoreCase(args[0])) {
            var trait = npc.getTraitOptional(CasterTrait.class);
            if(trait.isPresent()) {
                List<SpellCastRule> rules = trait.get().getRules();
                if(rules.isEmpty()) {
                    sender.sendMessage("§7This NPC does not have any cast rule.");
                } else {
                    sender.sendMessage("§aFound " + rules.size() + " cast rule" + (rules.size()>1?"s":"") + " (§e" + rules.size() + "§a) :");
                    int i = 0;
                    for (SpellCastRule rule : rules) {
                        sender.sendMessage("§7- [§f"+(i++)+"§7] §e" + rule);
                    }
                }
            } else {
               sender.sendMessage("§cThis NPC is not a caster.");
            }
            return true;
        }

        if("remove".equalsIgnoreCase(args[0])) {
            if(args.length < 2) {
                sender.sendMessage("§cMissing argument: rule index.");
                return true;
            }
            CasterTrait trait = npc.getOrAddTrait(CasterTrait.class);
            int index;
            try {
                index = Integer.parseInt(args[1]);
            } catch(NumberFormatException e) {
                sender.sendMessage("§cInvalid index : bad number format.");
                return true;
            }
            if(index < 0 || index >= trait.getRules().size()) {
                sender.sendMessage("§cInvalid index : out of bound..");
                return true;
            }
            trait.getRules().remove(index);
            sender.sendMessage("§aSpell rule §e"+index+"§a removed.");
            return true;
        }

        if(args.length < 2) {
            sender.sendMessage("§cMissing argument: spell ID.");
            return true;
        }
        Spell spell = UltimateSpellSystem.getSpellsManager().getSpell(args[1]);
        if(spell == null) {
            sender.sendMessage("§cUnknown spell: '§4" + args[1] + "§c'.");
            return true;
        }

        if("cast".equalsIgnoreCase(args[0])) {
            npc.getOrAddTrait(CasterTrait.class).cast(spell);
            sender.sendMessage("§aThe NPC executed the spell.");
            return true;
        }

        if("add".equalsIgnoreCase(args[0])) {
            // Cooldown
            String cooldown;
            if(args.length > 2) {
                cooldown = args[2];
                if(DurationHelper.parse(cooldown, null) == null) {
                    sender.sendMessage("Invalid duration format: '" + cooldown + "'.");
                    return true;
                }
            } else {
                cooldown = "10s";
            }

            // Condition
            String condition;
            if(args.length > 3) {
                StringJoiner joiner = new StringJoiner(" ");
                for(int i = 3; i < args.length; i++) {
                    joiner.add(args[i]);
                }
                String concat = joiner.toString();
                if(concat.startsWith("\"") && concat.endsWith("\""))
                    condition = concat.substring(1, concat.length() - 1);
                else condition = concat;
                // parse
                try {
                    UltimateSpellSystemDSL.parseExpression(condition);
                } catch (Exception e) {
                    sender.sendMessage("§cInvalid USS syntax: " + e.getMessage());
                    return true;
                }
            } else {
                condition = "";
            }

            // Register
            SpellCastRule rule = new SpellCastRule(spell.getName(), cooldown, condition);
            npc.getOrAddTrait(CasterTrait.class).getRules().add(rule);

            // Confirm
            sender.sendMessage("§aNew spell rule registered.");
            sender.sendMessage("§7Spell ID = §e" + spell.getName());
            sender.sendMessage("§7Cooldown = §e" + cooldown);
            sender.sendMessage("§7Condition = " + (condition.isEmpty() ? "§7<none>" : "§f"+condition));
            return true;
        }

        sender.sendMessage("§cInvalid sub-command. Use one of: " + ARGS);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if(args.length == 1) {
            String arg0 = args[0].toLowerCase();
            return ARGS.stream()
                    .filter(a -> a.contains(arg0))
                    .toList();
        }

        NPC npc = CitizensAPI.getDefaultNPCSelector().getSelected(sender);
        if(args.length == 2) {
            if("remove".equalsIgnoreCase(args[0])) {
                int size = npc.getTraitOptional(CasterTrait.class).toJavaUtil().map(t -> t.getRules().size()).orElse(0);
                return IntStream.range(0, size)
                        .mapToObj(String::valueOf)
                        .filter(i -> i.contains(args[1]))
                        .toList();
            }
            if("add".equalsIgnoreCase(args[0]) || "cast".equalsIgnoreCase(args[0])) {
                return UltimateSpellSystem.getSpellsManager().spellIds()
                        .stream()
                        .filter(s -> s.contains(args[1]))
                        .toList();
            }
        }
        return List.of();
    }

    private void sendHelp(@NotNull CommandSender sender) {
        sender.sendMessage("§e======== USS demo ========");
        sender.sendMessage("§e");
        sender.sendMessage("§7Citizens NPC can cast spell. You can setup this by adding a cooldown and a duration.");
        sender.sendMessage("§7Every command requires a NPC to be selected.");
        sender.sendMessage("§e");
        sender.sendMessage("§e/spellcaster§b help§7 : Display this menu.");
        sender.sendMessage("§e/spellcaster§b list§7 : List the spell rules on an NPC.");
        sender.sendMessage("§e/spellcaster§b remove §d<index>§7 : Remove the rule of an index.");
        sender.sendMessage("§e/spellcaster§b add §d<spell_id> §b[cooldown] [uss condition]§7 : Add a rule. §7The spell ID must match a valid USS spell. The cooldown must have a format <int>[t/s/m/h]. The condition is a valid USS expression.");
        sender.sendMessage("§e==========================");
    }
}
